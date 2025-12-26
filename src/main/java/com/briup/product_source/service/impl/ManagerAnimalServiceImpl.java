package com.briup.product_source.service.impl;

import com.briup.product_source.dao.ManagerAnimalMapper;
import com.briup.product_source.dao.ManagerBatchMapper;
import com.briup.product_source.dao.ManagerHurdlesMapper;
import com.briup.product_source.dao.ext.ManagerAnimalExtMapper;
import com.briup.product_source.exception.ServiceException;
import com.briup.product_source.pojo.ManagerAnimal;
import com.briup.product_source.pojo.ManagerBatch;
import com.briup.product_source.pojo.ManagerHurdles;
import com.briup.product_source.pojo.ext.ManagerAnimalExt;
import com.briup.product_source.result.ResultCode;
import com.briup.product_source.service.ManagerAnimalService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
public class ManagerAnimalServiceImpl implements ManagerAnimalService {
    @Autowired
    private ManagerAnimalExtMapper animalExtMapper;

    @Autowired
    private ManagerAnimalMapper animalMapper;

    @Autowired
    private ManagerBatchMapper batchMapper;

    @Autowired
    private ManagerHurdlesMapper hurdlesMapper;

    @Override
    public PageInfo<ManagerAnimalExt> findByPage(Integer pageNum,
                                                 Integer pageSize,
                                                 String aHealthy,
                                                 String aGender) {
        //1.开启分页查询
        PageHelper.startPage(pageNum, pageSize);

        //2.条件查询
        List<ManagerAnimalExt> animalRelated =
                animalExtMapper.selectAnimalRelated(aHealthy, aGender);

        //3.封装分页对象并返回
        PageInfo<ManagerAnimalExt> pageInfo = new PageInfo(animalRelated);

        return pageInfo;
    }

    //新增或修改动物，该功能业务较为复杂，涉及批次、栏圈模块，请静心认真分析
    @Override
    public void saveOrUpdate(ManagerAnimal animal) {
        //1.参数校验,体重不为空(其他字段也非空，前端已处理，此处不需要处理)
        if (animal == null || !StringUtils.hasText(animal.getAWeight())) {
            throw new ServiceException(ResultCode.PARAM_IS_EMPTY);
        }

        //2.判断栏圈是否有效存在
        // 新栏圈编号
        String aHurdlesIdNew = animal.getAHurdlesId();
        ManagerHurdles hurdlesNew = hurdlesMapper.selectByPrimaryKey(aHurdlesIdNew);
        if (hurdlesNew == null) {
            throw new ServiceException(ResultCode.HURDLES_NOT_EXIST);
        }

        // ✅ 【新增】防御性校验：先看满了没！
// 如果状态是"已满"，或者 存栏量 >= 最大值，严禁入栏！
        if ("已满".equals(hurdlesNew.getHFull()) || hurdlesNew.getHSaved() >= hurdlesNew.getHMax()) {
            // 抛出一个异常，告诉前端：这个圈满了，换一个！
            // (你可能需要在 ResultCode 里加一个 HURDLES_IS_FULL)
            throw new ServiceException(ResultCode.HURDLES_IS_FULL);
        }

        //3.添加或者修改动物信息
        String aAnimalId = animal.getAAnimalId();
        if(StringUtils.hasText(aAnimalId)) {
            //3.1 有id->更新操作
            // a.根据id查询动物信息
            ManagerAnimal animalFromDB = animalMapper.selectByPrimaryKey(aAnimalId);
            if (animalFromDB == null)
                throw new ServiceException(ResultCode.ANIMAL_NOT_EXIST);

            // b.修改动物信息
            int result = animalMapper.updateByPrimaryKey(animal);
            if (result == 0)
                throw new ServiceException(ResultCode.FAIL);

            // c.判断是否修改了栏圈信息
            String aHurdlesIdOld = animalFromDB.getAHurdlesId();
            if (!aHurdlesIdOld.equals(aHurdlesIdNew)) {
                //动物栏圈发生改变
                // 1.新栏圈动物数量 + 1，并重置空满状态
                // 动物数量 + 1
                Integer hSaved = hurdlesNew.getHSaved();
                hurdlesNew.setHSaved(hSaved + 1);

                // 重置新栏圈空满状态
                if(hSaved + 1 == hurdlesNew.getHMax()) {
                    hurdlesNew.setHFull("已满");
                }

                // 执行更新操作
                hurdlesMapper.updateByPrimaryKey(hurdlesNew);

                // 2.老栏圈动物数量 - 1，并重置空满状态
                ManagerHurdles hurdlesOld = hurdlesMapper.selectByPrimaryKey(aHurdlesIdOld);
                // 动物数量 - 1
                hSaved = hurdlesOld.getHSaved();
                hurdlesOld.setHSaved(hSaved - 1);

                // 重置老栏圈空满状态
                if ("已满".equals(hurdlesOld.getHFull())) {
                    hurdlesOld.setHFull("未满");
                }

                // 执行更新
                hurdlesMapper.updateByPrimaryKey(hurdlesOld);
            }
        }else {
            // 3.2 无id->新增操作
            // a.批次有效判断
            ManagerBatch batchFromDB = batchMapper.selectByPrimaryKey(animal.getABatchId());
            if (batchFromDB == null) {
                throw new ServiceException(ResultCode.BATCH_NOT_EXIST);
            }

            // b.根据批次检疫状态，设置动物的养殖状态
            String bQuarantine = batchFromDB.getBQuarantine();
            if ("已检疫".equals(bQuarantine)) {
                animal.setAStatus("已检疫");
            } else {
                animal.setAStatus("养殖中");
            }

            // c.添加动物信息
            animal.setAAnimalId(UUID.randomUUID().toString().replace("-",""));
            int result = animalMapper.insert(animal);
            if(result == 0)
                throw new ServiceException(ResultCode.FAIL);

            // d.对应栏圈 动物数量 + 1，存储状态重置
            Integer hSaved = hurdlesNew.getHSaved();
            hurdlesNew.setHSaved(hSaved + 1);
            if(hSaved+1 == hurdlesNew.getHMax()) {
                hurdlesNew.setHFull("已满");
            }
            hurdlesMapper.updateByPrimaryKey(hurdlesNew);
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class) // 开启事务
    public void deleteById(String id) {
        // 1. 查询动物是否存在
        ManagerAnimal animal = animalMapper.selectByPrimaryKey(id);
        if (animal == null) {
            throw new ServiceException(ResultCode.DATA_IS_EMPTY);
        }

        // 2. 获取该动物所在的栏圈ID
        String hurdlesId = animal.getAHurdlesId();
        if (StringUtils.hasText(hurdlesId)) {
            ManagerHurdles hurdle = hurdlesMapper.selectByPrimaryKey(hurdlesId);
            if (hurdle != null) {
                // 3. 维护栏圈数据：存栏量 - 1
                int currentSaved = hurdle.getHSaved() == null ? 0 : hurdle.getHSaved();
                if (currentSaved > 0) {
                    hurdle.setHSaved(currentSaved - 1);
                    // 4. 如果之前是"已满"，现在减了一个，肯定变成"未满"
                    //    或者简单点，只要没达到Max，就是未满 (这里直接置为未满即可，除非Max是0)
                    if ("已满".equals(hurdle.getHFull())) {
                        hurdle.setHFull("未满");
                    }
                    hurdlesMapper.updateByPrimaryKey(hurdle);
                }
            }
        }

        // 5. 执行物理删除
        animalMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<String> ids) {
        // 复用单删逻辑，确保每一条删除都能正确扣减库存
        for (String id : ids) {
            deleteById(id);
        }
    }
}

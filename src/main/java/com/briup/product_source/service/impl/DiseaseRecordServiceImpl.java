package com.briup.product_source.service.impl;

import com.briup.product_source.dao.DiseaseRecordMapper;
import com.briup.product_source.dao.ManagerAnimalMapper;
import com.briup.product_source.dao.ManagerDiseaseMapper;
import com.briup.product_source.dao.ext.DiseaseRecordExtMapper;
import com.briup.product_source.exception.ServiceException;
import com.briup.product_source.pojo.DiseaseRecord;
import com.briup.product_source.pojo.ManagerAnimal;
import com.briup.product_source.pojo.ManagerDisease;
import com.briup.product_source.pojo.ext.DiseaseRecordExt;
import com.briup.product_source.result.ResultCode;
import com.briup.product_source.service.DiseaseRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class DiseaseRecordServiceImpl implements DiseaseRecordService {
    @Autowired
    private DiseaseRecordExtMapper dRecordExtMapper;

    @Autowired
    private ManagerDiseaseMapper diseaseMapper;

    @Autowired
    private ManagerAnimalMapper animalMapper;

    @Autowired
    private DiseaseRecordMapper dRecordMapper;

    @Override
    public List<ManagerDisease> findAllDiseases() {
        List<ManagerDisease> list = diseaseMapper.selectAll();
        return list;
    }

    @Override
    public PageInfo<DiseaseRecordExt> findByPage(Integer pageNum, Integer pageSize, String drStatus, Integer drDId) {
        //1.开启分页查询
        PageHelper.startPage(pageNum, pageSize);

        //2.条件查询
        List<DiseaseRecordExt> animals
                = dRecordExtMapper.selectDiseasedAnimal(drStatus, drDId);

        //3.封装分页对象并返回
        PageInfo pageInfo = new PageInfo(animals);

        return pageInfo;
    }

    @Override
    public void saveOrUpdate(DiseaseRecord record) {
        String animalId = record.getDrAnimalId();
        //1.参数校验[动物编号、病症描述不能为空]
        if (!StringUtils.hasText(animalId) ||
                !StringUtils.hasText(record.getDrDesc())) {
            throw new ServiceException(ResultCode.PARAM_IS_EMPTY);
        }

        //2.动物校验
        ManagerAnimal animalFromDB = animalMapper.selectByPrimaryKey(animalId);
        //2.1 动物不存在抛出异常
        if (animalFromDB == null)
            throw new ServiceException(ResultCode.FAIL);
        //2.2 动物状态不是"养殖中"，抛出异常
        if(!"养殖中".equals(animalFromDB.getAStatus())){
            throw new ServiceException(ResultCode.ANIMAL_IS_NOT_IN_BREEDING);
        }

        //3.添加或者修改诊疗记录
        // sql语句执行后，返回受影响的行数result
        int result;
        Integer drId = record.getDrId();
        String drStatus = record.getDrStatus();
        if (drId != null) {
            //3.1 有id->更新操作
            if (dRecordMapper.selectByPrimaryKey(drId) == null)
                throw new ServiceException(ResultCode.DATA_IS_EMPTY);

            result = dRecordMapper.updateByPrimaryKey(record);
        } else {
            //3.2 无id->新增操作
            // 未传诊疗状态，默认为未治疗
            if (!StringUtils.hasText(drStatus)) {
                record.setDrStatus("未治疗");
            }

            //新增记录
            result = dRecordMapper.insert(record);
        }

        // 添加病症记录失败
        if (result == 0) {
            throw new ServiceException(ResultCode.FAIL);
        }

        //4.添加病症记录成功，根据诊疗状态修改动物健康状态
        String healthy = "健康";
        if (!"已治疗".equals(drStatus)) {
            healthy = "生病";
        }
        if (animalMapper.updateHealthyByAnimalId(healthy, animalId) == 0) {
            throw new ServiceException(ResultCode.FAIL);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        // 1. 参数校验
        if (id == null) {
            throw new ServiceException(ResultCode.PARAM_IS_EMPTY);
        }

        // 2. 执行删除
        int result = dRecordMapper.deleteByPrimaryKey(id);

        // 3. 结果验证
        if (result == 0) {
            throw new ServiceException(ResultCode.DATA_IS_EMPTY);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Integer> ids) {
        // 1. 参数校验
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException(ResultCode.PARAM_IS_EMPTY);
        }

        // 2. 执行批量删除
        int result = dRecordMapper.deleteBatch(ids);

        // 3. 结果验证
        if (result == 0) {
            throw new ServiceException(ResultCode.FAIL);
        }
    }
}

package com.briup.product_source.service.impl;

import com.briup.product_source.dao.ManagerBatchMapper;
import com.briup.product_source.dao.QuarantineRegistrationMapper;
import com.briup.product_source.exception.ServiceException;
import com.briup.product_source.pojo.ManagerBatch;
import com.briup.product_source.pojo.QuarantineRegistration;
import com.briup.product_source.result.ResultCode;
import com.briup.product_source.service.QuarantineRegistrationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class QuarantineRegistrationServiceImpl implements QuarantineRegistrationService {
    @Autowired
    private QuarantineRegistrationMapper qrMapper;

    @Autowired
    private ManagerBatchMapper batchMapper;

    @Override
    public PageInfo<QuarantineRegistration> findByPage(Integer pageNum,
                                                       Integer pageSize,
                                                       String mechanism,
                                                       String bQualified) {
        //1.开启分页查询
        PageHelper.startPage(pageNum, pageSize);

        //2.执行条件查询
        List<QuarantineRegistration> list = qrMapper.selectAllRecord(mechanism, bQualified);

        //3.封装分页对象并返回
        PageInfo<QuarantineRegistration> info = new PageInfo<>(list);

        return info;
    }

    @Override
    public void saveOrUpdate(QuarantineRegistration qr) {
        //1.参数校验
        if (qr == null ||
                !StringUtils.hasText(qr.getGrMechanism()) ||
                !StringUtils.hasText(qr.getGrImg())) {
            throw new ServiceException(ResultCode.PARAM_IS_EMPTY);
        }

        //2.批次校验【保证该批次存在】
        String grBatchId = qr.getGrBatchId();
        ManagerBatch batch = batchMapper.selectByPrimaryKey(grBatchId);
        if(batch == null)
            throw new ServiceException(ResultCode.BATCH_NOT_EXIST);

        //3.执行更新或更新，返回受影响的行数result
        int result;
        if (qr.getGrId() != null) {
            //3.1 有id->更新操作
            result = qrMapper.updateByPrimaryKey(qr);
        } else {
            //3.2 无id->新增操作
            result = qrMapper.insert(qr);
        }

        //4.结果判断：如果影响行数为0，则抛出异常
        if (result == 0) {
            throw new ServiceException(ResultCode.FAIL);
        }

        //5.修改指定批次检疫状态为"已检疫"及检疫合格状态 bQualified
        String bQualified = qr.getBQualified();
        if (batchMapper.updateQualifiedById(bQualified, grBatchId) == 0) {
            throw new ServiceException(ResultCode.FAIL);
        }
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer grId) {
        // 1. 校验参数
        if (grId == null) {
            throw new ServiceException(ResultCode.PARAM_IS_EMPTY);
        }

        // 2. 执行删除
        int result = qrMapper.deleteByPrimaryKey(grId);

        // 3. 判断结果
        if (result == 0) {
            throw new ServiceException(ResultCode.DATA_IS_EMPTY); // 或者 FAIL
        }

        // 可选：这里可以添加逻辑，如果删除检疫单，是否需要把对应批次的状态改回"未检疫"？
        // 暂时保持简单，只删除记录。
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Integer> ids) {
        // 1. 校验参数
        if (ids == null || ids.isEmpty()) {
            throw new ServiceException(ResultCode.PARAM_IS_EMPTY);
        }

        // 2. 执行批量删除
        int result = qrMapper.deleteBatch(ids);

        // 3. 判断结果
        if (result == 0) {
            throw new ServiceException(ResultCode.FAIL);
        }
    }
}

package com.briup.product_source.service.impl;

import com.briup.product_source.dao.ManagerBatchMapper;
import com.briup.product_source.exception.ServiceException;
import com.briup.product_source.pojo.ManagerBatch;
import com.briup.product_source.result.ResultCode;
import com.briup.product_source.service.ManagerBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerBatchServiceImpl implements ManagerBatchService {
    @Autowired
    private ManagerBatchMapper batchMapper;

    @Override
    public List<ManagerBatch> findAllUnquarantined() {
        List<ManagerBatch> list = batchMapper.selectAllUnquarantined();

        if(list == null || list.isEmpty())
            throw new ServiceException(ResultCode.DATA_IS_EMPTY);

        return list;
    }

    @Override
    public List<ManagerBatch> findAll() {
        List<ManagerBatch> list = batchMapper.selectAllBatches();
        return list;
    }
}

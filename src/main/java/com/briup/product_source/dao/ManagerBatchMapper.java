package com.briup.product_source.dao;

import com.briup.product_source.pojo.ManagerBatch;

import java.util.List;

public interface ManagerBatchMapper {
    //查询所有未检疫的批次信息
    List<ManagerBatch> selectAllUnquarantined();

    //获取批次信息
    ManagerBatch selectByPrimaryKey(String batchId);

    //更新批次表中检疫状态
    int updateQualifiedById(String bQualified, String batchId);

    //查询所有批次信息
    List<ManagerBatch> selectAllBatches();
}
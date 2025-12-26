package com.briup.product_source.service;

import com.briup.product_source.pojo.DiseaseRecord;
import com.briup.product_source.pojo.ManagerDisease;
import com.briup.product_source.pojo.ext.DiseaseRecordExt;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface DiseaseRecordService {
    //查询所有病症信息
    List<ManagerDisease> findAllDiseases();

    //分页加条件查询病症记录信息（含病症信息）
    PageInfo<DiseaseRecordExt> findByPage(Integer pageNum,
                                          Integer pageSize,
                                          String drStatus,
                                          Integer drDId);

    //新增|修改 治疗记录
    void saveOrUpdate(DiseaseRecord record);

    // 【修改】根据id删除病症信息 (注意：改为 Integer)
    void deleteById(Integer id);

    // 【修改】批量删除病症信息 (注意：改为 Integer)
    void deleteBatch(List<Integer> ids);
}

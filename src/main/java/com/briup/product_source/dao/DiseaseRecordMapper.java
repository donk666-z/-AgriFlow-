package com.briup.product_source.dao;

import com.briup.product_source.pojo.DiseaseRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DiseaseRecordMapper {

    // 根据主键查询治疗记录
    DiseaseRecord selectByPrimaryKey(Integer drId);

    //更新指定治疗记录
    int updateByPrimaryKey(DiseaseRecord record);

    //新增治疗记录
    int insert(DiseaseRecord record);

    // 【新增】根据ID删除单条记录
    int deleteByPrimaryKey(Integer drId);

    // 【新增】批量删除
    int deleteBatch(List<Integer> ids);
}
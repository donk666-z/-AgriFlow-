package com.briup.product_source.dao.ext;

import com.briup.product_source.pojo.ext.DiseaseRecordExt;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface DiseaseRecordExtMapper {

    //条件查询 治疗记录信息(含病症信息)
    List<DiseaseRecordExt> selectDiseasedAnimal(String drStatus, Integer drDId);
}
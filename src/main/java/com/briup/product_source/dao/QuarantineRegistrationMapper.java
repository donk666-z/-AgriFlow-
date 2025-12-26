package com.briup.product_source.dao;

import com.briup.product_source.pojo.QuarantineRegistration;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface QuarantineRegistrationMapper {
    //条件查询检疫记录
    List<QuarantineRegistration> selectAllRecord(String mechanism, String bQualified);

    //更新
    int updateByPrimaryKey(QuarantineRegistration qr);

    //新增操作
    int insert(QuarantineRegistration qr);

    // 【新增】根据主键删除单条记录
    int deleteByPrimaryKey(Integer grId);

    // 【新增】批量删除
    int deleteBatch(List<Integer> ids);
}
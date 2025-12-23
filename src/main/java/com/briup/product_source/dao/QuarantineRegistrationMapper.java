package com.briup.product_source.dao;

import com.briup.product_source.pojo.QuarantineRegistration;

import java.util.List;

public interface QuarantineRegistrationMapper {
    //条件查询检疫记录
    List<QuarantineRegistration> selectAllRecord(String mechanism, String bQualified);

    //更新
    int updateByPrimaryKey(QuarantineRegistration qr);

    //新增操作
    int insert(QuarantineRegistration qr);
}
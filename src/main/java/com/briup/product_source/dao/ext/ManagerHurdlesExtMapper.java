package com.briup.product_source.dao.ext;

import com.briup.product_source.pojo.ext.ManagerHurdlesExt;

import java.util.List;

public interface ManagerHurdlesExtMapper {

    //多条件查询栏圈信息
    List<ManagerHurdlesExt> findHurdlesWithHouseByConditions(String hName,
                                                             Integer hMax,
                                                             String fhName,
                                                             String hEnable);
}

package com.briup.product_source.dao.ext;

import com.briup.product_source.pojo.ext.ManagerAnimalExt;

import java.util.List;

public interface ManagerAnimalExtMapper {

    //条件查询动物信息(含栏圈名称、栏舍名称及批次信息)
    List<ManagerAnimalExt> selectAnimalRelated(String aHealthy, String aGender);
}

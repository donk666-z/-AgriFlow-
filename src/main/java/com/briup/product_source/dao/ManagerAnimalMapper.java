package com.briup.product_source.dao;

import com.briup.product_source.pojo.ManagerAnimal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Mapper
@Repository
public interface ManagerAnimalMapper {

    //查询指定动物
    ManagerAnimal selectByPrimaryKey(String drAnimalId);

    //更新动物健康状态
    int updateHealthyByAnimalId(String healthy, String animalId);

    //更新动物信息
    int updateByPrimaryKey(ManagerAnimal animal);

    //新增动物
    int insert(ManagerAnimal animal);

    //统计各区间动物数量
    Map<String, Integer> countWeight();

    // 【新增】根据主键删除
    int deleteByPrimaryKey(String aAnimalId);

    // 【新增】批量删除
    int deleteBatchIds(@Param("ids") List<String> ids);
}
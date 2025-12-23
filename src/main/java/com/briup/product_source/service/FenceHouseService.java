package com.briup.product_source.service;

import com.briup.product_source.pojo.ManagerFenceHouse;
import com.briup.product_source.pojo.ext.ManagerFenceHouseExt;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface FenceHouseService {
    /**
     * 多条件分页查询
     * @param pageNum  当前页码
     * @param pageSize 每页大小
     * @param fhName   栏舍名称
     * @return
     */
    PageInfo<ManagerFenceHouse> findByPage(int pageNum, int pageSize, String fhName);

    /**
     * 根据id查询栏舍信息及其对应的栏圈信息
     * @param id 栏舍id
     * @return  ManagerFenceHouseExtend类型是一个自定义类型，用来实现1对多的映射。思考如何设计这个类？
     */
    ManagerFenceHouseExt findById(String id);

    /**
     * 保存或者更新
     * @param house 栏舍信息对象
     * 注意：如果id存在则为修改，不存在则为新增
     */
    void saveOrUpdate(ManagerFenceHouse house);

    //删除指定id的栏舍信息
    void removeById(String fhId);

    //批量删除栏舍
    void removeBatch(List<String> ids);

    //查询所有栏舍信息(单表)
    List<ManagerFenceHouse> findAll();
}

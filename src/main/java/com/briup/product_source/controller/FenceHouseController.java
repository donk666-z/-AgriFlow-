package com.briup.product_source.controller;

import com.briup.product_source.pojo.ManagerFenceHouse;
import com.briup.product_source.pojo.ext.ManagerFenceHouseExt;
import com.briup.product_source.result.Result;
import com.briup.product_source.service.FenceHouseService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "栏舍管理模块")
@RestController
@RequestMapping("/fenceHouse")
public class FenceHouseController {
    @Autowired
    private FenceHouseService houseService;

    @ApiOperation("分页多条件查询栏舍信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "当前页码", defaultValue = "1", required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页大小", defaultValue = "10", required = true),
            @ApiImplicitParam(name = "fhName",value = "栏舍名称")
    })
    @GetMapping
    public Result queryByConditionsAndPage(Integer pageNum, Integer pageSize, String fhName) {
        PageInfo<ManagerFenceHouse> pageInfo = houseService.findByPage(pageNum, pageSize, fhName);
        return Result.success(pageInfo);
    }

    @ApiOperation("根据栏舍编号查询栏舍信息及栏圈信息接口")
    @GetMapping("/{fhId}")
    public Result queryRelativeDetails(@PathVariable String fhId) {
        ManagerFenceHouseExt houseExt = houseService.findById(fhId);
        return Result.success(houseExt);
    }

    @ApiOperation("新增或修改栏舍接口")
    @PostMapping("/saveOrUpdate")
    public Result reviseFenceHouses(@RequestBody ManagerFenceHouse house) {
        houseService.saveOrUpdate(house);
        return Result.success("操作成功");
    }

    @ApiOperation("根据栏舍编号删除栏舍信息接口")
    @ApiImplicitParam(name = "fhId", value = "栏舍编号",
            required = true, paramType = "path")
    @DeleteMapping("/{fhId}")
    public Result removeById(@PathVariable String fhId) {
        houseService.removeById(fhId);
        return Result.success();
    }

    @ApiOperation("批量删除栏舍接口")
    @DeleteMapping("/deleteByIdAll")
    public Result removeBatch(@RequestBody List<String> ids) {
        System.out.println("ids: " + ids);
        houseService.removeBatch(ids);
        return Result.success();
    }

    @ApiOperation("查询所有的栏舍信息")
    @GetMapping("/queryAll")
    public Result queryAll() {
        List<ManagerFenceHouse> list = houseService.findAll();
        return Result.success(list);
    }
}
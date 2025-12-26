package com.briup.product_source.controller;

import com.briup.product_source.pojo.ManagerAnimal;
import com.briup.product_source.pojo.ext.ManagerAnimalExt;
import com.briup.product_source.result.Result;
import com.briup.product_source.service.ManagerAnimalService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "动物管理模块")
@RestController
@RequestMapping("/animal")
public class AnimalController {
    @Autowired
    private ManagerAnimalService animalService;

    @ApiOperation("分页多条件查询动物信息(含栏圈名称、栏舍名称及批次信息)")
    @GetMapping
    public Result getAnimalRelated(Integer pageNum,
                                   Integer pageSize,
                                   String aHealthy,
                                   String aGender) {
        PageInfo<ManagerAnimalExt> info =
                animalService.findByPage(pageNum, pageSize, aHealthy, aGender);
        return Result.success(info);
    }

    @ApiOperation("新增或修改动物接口")
    @PostMapping("/saveOrUpdate")
    public Result reviseAnimalInfo(@RequestBody ManagerAnimal animal) {
        animalService.saveOrUpdate(animal);
        return Result.success();
    }

    @ApiOperation("根据编号删除动物")
    @DeleteMapping("/deleteById/{id}")
    public Result removeById(@PathVariable String id) {
        animalService.deleteById(id);
        return Result.success();
    }

    @ApiOperation("批量删除动物")
    @DeleteMapping("/deleteByIdAll")
    public Result removeBatch(@RequestBody List<String> ids) {
        animalService.deleteBatch(ids);
        return Result.success();
    }
}

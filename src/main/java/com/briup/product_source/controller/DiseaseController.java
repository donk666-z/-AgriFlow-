package com.briup.product_source.controller;

import com.briup.product_source.pojo.DiseaseRecord;
import com.briup.product_source.pojo.ManagerDisease;
import com.briup.product_source.pojo.ext.DiseaseRecordExt;
import com.briup.product_source.result.Result;
import com.briup.product_source.service.DiseaseRecordService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "病症记录模块")
@RestController
@RequestMapping("/diseaseRecord")
public class DiseaseController {
    @Autowired
    private DiseaseRecordService diseaseService;

    @ApiOperation("查询所有病症类型信息")
    @GetMapping("/queryAllDisease")
    public Result getAnimalRelated() {
        List<ManagerDisease> result = diseaseService.findAllDiseases();
        return Result.success(result);
    }

    @ApiOperation("分页多条件查询病症记录")
    @GetMapping
    public Result getAnimalRelated(Integer pageNum, Integer pageSize,
                                   String drStatus, Integer drDId) {
        PageInfo<DiseaseRecordExt> result
                = diseaseService.findByPage(pageNum, pageSize, drStatus, drDId);
        return Result.success(result);
    }

    @ApiOperation("新增或更新病症记录")
    @PostMapping("/saveOrUpdate")
    public Result reviseDiseaseRecord(@RequestBody DiseaseRecord record) {
        diseaseService.saveOrUpdate(record);
        return Result.success();
    }
}

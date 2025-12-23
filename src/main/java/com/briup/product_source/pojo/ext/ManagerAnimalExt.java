package com.briup.product_source.pojo.ext;

import com.briup.product_source.pojo.ManagerAnimal;
import com.briup.product_source.pojo.ManagerBatch;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ManagerAnimalExt extends ManagerAnimal {
    //栏圈名称
    @JsonProperty("managerHurdles")
    private String managerHurdles;

    //栏舍名称
    @JsonProperty("managerFenceHouse")
    private String managerFenceHouse;

    //二维码
    @JsonProperty("aBackup3")
    private String url;

    //所属批次
    @JsonProperty("managerBatch")
    private ManagerBatch managerBatch;
}

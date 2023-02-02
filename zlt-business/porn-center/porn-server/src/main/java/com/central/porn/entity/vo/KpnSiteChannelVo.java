package com.central.porn.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("站点频道")
public class KpnSiteChannelVo implements Serializable {
    @ApiModelProperty(value = "频道名称(中文)")
    private Long id;

    @ApiModelProperty(value = "频道名称(中文)")
    private String nameZh;

    @ApiModelProperty(value = "频道名称(英文)")
    private String nameEn;

    @ApiModelProperty(value = "频道名称(柬文)")
    private String nameKh;

    @ApiModelProperty(value = "排序(越大越靠前)")
    private Long sort;

    @ApiModelProperty(value = "是否固定频道 0自定义,1内置固定")
    private Boolean isStable;
}
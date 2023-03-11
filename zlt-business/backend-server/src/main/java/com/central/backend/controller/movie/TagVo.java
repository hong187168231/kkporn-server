package com.central.backend.controller.movie;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("标签信息")
public class TagVo implements Serializable {

    @ApiModelProperty("标签id")
    private Long id;

    @ApiModelProperty("标签名(多语言)")
    private String name;
}

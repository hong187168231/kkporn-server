package com.central.porn.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("站点广告")
public class KpnSiteAdvertiseVo implements Serializable {

    @ApiModelProperty(value = "广告名称")
    private String name;

    @ApiModelProperty(value = "投放平台(H5,PC)")
    private String device;

    @ApiModelProperty(value = "投放位置 1首页轮播图,2首页平台展示,3首页专题广告,4福利,5游戏轮播图,6游戏广告")
    private Integer position;

    @ApiModelProperty(value = "排序(越大越靠前)")
    private Long sort;

    @ApiModelProperty(value = "状态 0下架,1上架")
    private Integer status;

    @ApiModelProperty(value = "开始时间")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "广告url")
    private String url;

    @ApiModelProperty(value = "链接url")
    private String linkUrl;

    @ApiModelProperty(value = "备注")
    private String remark;
}
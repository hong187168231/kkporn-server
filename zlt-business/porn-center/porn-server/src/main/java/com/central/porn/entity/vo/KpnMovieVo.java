package com.central.porn.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("影片详细信息")
public class KpnMovieVo extends KpnSiteMovieBaseVo implements Serializable {

    @ApiModelProperty("影片url")
    private String url;

    @ApiModelProperty("演员id")
    private Long actorId;

    @ApiModelProperty("影片收藏状态 false未收藏,true已收藏")
    private Boolean hasFavor = false;

}

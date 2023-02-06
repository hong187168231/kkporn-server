package com.central.common.model;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("kpn_site_movie")
@ApiModel("站点影片")
public class KpnSiteMovie extends SuperEntity {

    @ApiModelProperty(value = "站点id")
    private Long siteId;

    @ApiModelProperty(value = "站点编码")
    private String siteCode;

    @ApiModelProperty(value = "站点名称")
    private String siteName;

    @ApiModelProperty(value = "频道名称(中文)")
    private Long movieId;

    @ApiModelProperty(value = "播放量")
    private Long vv;

    @ApiModelProperty(value = "频道状态 0下架,1上架")
    private Long favorites;

    @ApiModelProperty(value = "状态 0待发布,1上架,2下架")
    private Integer status;

}

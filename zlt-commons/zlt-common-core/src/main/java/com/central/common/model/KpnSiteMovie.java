package com.central.common.model;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @ApiModelProperty(value = "演员id")
    private Long actorId;

    @ApiModelProperty(value = "演员创建时间")
    private DateTime actorCreateTime;

    @ApiModelProperty(value = "影片id")
    private Long movieId;

    @ApiModelProperty(value = "时长")
    private String duration;

    @ApiModelProperty("付费类型 0/false:免费,1/true:付费")
    private Boolean payType;

    @ApiModelProperty(value = "影片站点播放量")
    private Long vv;

    @ApiModelProperty(value = "影片站点收藏量")
    private Long favorites;

    @ApiModelProperty(value = "状态 0待发布,1上架,2下架")
    private Integer status;

}

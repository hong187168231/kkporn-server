package com.central.backend.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel("站点影片")
public class KpnSiteMovieVO {
    @ApiModelProperty(value = "站点影片ID")
    private Long id;
    @ApiModelProperty(value = "影片id")
    private Long movieId;
    @ApiModelProperty(value = "影片名称-中文")
    private String nameZh;
    @ApiModelProperty(value = "影片名称-英文")
    private String nameEn;
    @ApiModelProperty(value = "影片名称-柬文")
    private String nameKh;
    @ApiModelProperty(value = "演员id")
    private Long actorId;
    @ApiModelProperty(value = "演员中文名")
    private String actorNameZh;
    @ApiModelProperty(value = "演员英文名")
    private String actorNameEn;
    @ApiModelProperty(value = "演员柬文名")
    private String actorNameKh;
    @ApiModelProperty(value = "影片站点播放量")
    private Long vv;
    @ApiModelProperty(value = "影片站点收藏量")
    private Long favorites;
    @ApiModelProperty("付费类型 0/false:免费,1/true:付费")
    private Boolean payType;
    @ApiModelProperty(value = "状态 0待发布,1上架,2下架")
    private Integer status;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    @ApiModelProperty("播放地址")
    private String coverUrl;
}

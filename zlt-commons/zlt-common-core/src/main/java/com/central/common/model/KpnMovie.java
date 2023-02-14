package com.central.common.model;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("kpn_movie")
public class KpnMovie extends SuperEntity {
    @ApiModelProperty("播放地址")
    private String url;
    @ApiModelProperty("唯一编码 20位")
    private String code;
    @ApiModelProperty("播放地址")
    private String coverUrl;
    @ApiModelProperty("中文名称")
    private String nameZh;
    @ApiModelProperty("英文名称")
    private String nameEn;
    @ApiModelProperty("柬文名称")
    private String nameKh;
    @ApiModelProperty("国家")
    private String country;
    @ApiModelProperty("影片类型 0/false无码 1/true有码")
    private Boolean type;
    @ApiModelProperty("拍摄性质 special:专业拍摄,sneak:偷拍,selfie:自拍,other:其他")
    private String shootingType;
    @ApiModelProperty("字幕类型 no:无字幕,zh:中文,en:英文,zhen:中英,other:其他")
    private String subtitleType;
//    @ApiModelProperty("付费类型 0/false:免费,1/true:付费")
//    private Boolean payType;
    @ApiModelProperty("影片时长(HH:mm:ss 如00:10:02)")
    private String duration;
    @ApiModelProperty("番号")
    private String serialNumber;
    @ApiModelProperty("制作商")
    private String company;
    @ApiModelProperty("发行时间(默认为处理时间)")
    private Date publishTime;
    @ApiModelProperty("演员id")
    private Long actorId;
    @ApiModelProperty("演员中文名")
    private String actorNameZh;
    @ApiModelProperty("演员英文名")
    private String actorNameEn;
    @ApiModelProperty("演员柬文名")
    private String actorNameKh;
    @ApiModelProperty("影片状态 0/false:下架,1/true:上架")
    private Boolean status;
    @ApiModelProperty("影片处理状态 0/false:未处理 , 1/true:处理完成")
    private Boolean handleStatus;
    @ApiModelProperty("简介")
    private String remark;
    @ApiModelProperty("影片播放量(所有站点播放量之和)")
    private Long vv;
    @ApiModelProperty("影片收藏量(所有站点收藏量之和)")
    private Long favorites;
}

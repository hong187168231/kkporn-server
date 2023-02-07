package com.central.porn.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("影片")
public class KpnMovieVo extends LanguageNameVo implements Serializable {

    @ApiModelProperty("影片id")
    private Long id;

    @ApiModelProperty("唯一编码 20位")
    private String code;

    @ApiModelProperty("影片url")
    private String url;

    @ApiModelProperty("影片封面url")
    private String coverUrl;

    @ApiModelProperty("影片时长(HH:mm:ss 如00:10:02)")
    private String duration;

    @ApiModelProperty("站点播放量")
    private Long vv;

    @ApiModelProperty("站点收藏量")
    private Long favorites;

    @ApiModelProperty(value = "影片名称(多语言)")
    private String name;

//    @ApiModelProperty("影片中文名")
//    private String nameZh;
//
//    @ApiModelProperty("影片英文名")
//    private String nameEn;
//
//    @ApiModelProperty("影片柬文名")
//    private String nameKh;

    @ApiModelProperty("演员id")
    private Long actorId;

    @ApiModelProperty("演员中文名")
    private String actorNameZh;

    @ApiModelProperty("演员英文名")
    private String actorNameEn;

    @ApiModelProperty("演员柬文名")
    private String actorNameKh;

    @ApiModelProperty("简介")
    private String remark;

    @ApiModelProperty("影片标签")
    private List<KpnTagVo> tagVos;
}

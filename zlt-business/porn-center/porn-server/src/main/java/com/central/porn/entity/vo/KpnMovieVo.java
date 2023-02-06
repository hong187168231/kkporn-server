package com.central.porn.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("影片")
public class KpnMovieVo implements Serializable {

    @ApiModelProperty("影片id")
    private Long id;

    @ApiModelProperty("影片url")
    private String url;

    @ApiModelProperty("影片封面url")
    private String coverUrl;

    @ApiModelProperty("影片中文名")
    private String nameZh;

    @ApiModelProperty("影片英文名")
    private String nameEn;

    @ApiModelProperty("影片柬文名")
    private String nameKh;

    @ApiModelProperty("影片标签")
    private List<KpnTagVo> tagVos;
}

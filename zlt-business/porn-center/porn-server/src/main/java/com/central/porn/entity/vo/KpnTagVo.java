package com.central.porn.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("影片标签信息")
public class KpnTagVo implements Serializable {

    @ApiModelProperty("标签主键")
    private Long tagId;

    @ApiModelProperty("标签中文名")
    private String nameZh;

    @ApiModelProperty("标签中文名")
    private String nameEn;

    @ApiModelProperty("标签柬文名")
    private String nameKh;
}

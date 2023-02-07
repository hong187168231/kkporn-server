package com.central.porn.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LanguageNameVo {

    @JsonIgnore
    @ApiModelProperty(value = "名称(中文)")
    private String nameZh;

    @JsonIgnore
    @ApiModelProperty(value = "名称(英文)")
    private String nameEn;

    @JsonIgnore
    @ApiModelProperty(value = "名称(柬文)")
    private String nameKh;
}

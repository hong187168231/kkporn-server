package com.central.porn.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("会员频道")
public class KpnMemberChannelVo extends LanguageNameVo implements Serializable {
    @ApiModelProperty(value = "频道id")
    private Long channelId;

    @ApiModelProperty(value = "频道名称(多语言)")
    private String name;

    @ApiModelProperty(value = "排序(越大越靠前)")
    private Long sort;
}

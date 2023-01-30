package com.central.backend.model.co;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class I18nInfoPageMapperCo implements Serializable {

    private static final long serialVersionUID = 3258468554578L;

    @ApiModelProperty(value = "中文")
    private String zhCn;

    @ApiModelProperty(value = "英文")
    private String enUs;

    @ApiModelProperty(value = "高棉语")
    private String khm;

    @ApiModelProperty(value = "泰文")
    private String th;

    @ApiModelProperty(value = "越南语")
    private String vi;

    @ApiModelProperty(value = "所属 0=前台PC，1=后台 2=前台移动端 3=前台错误消息 4=后台错误消息")
    private Integer from;

}

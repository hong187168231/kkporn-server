package com.central.backend.co;

import com.central.common.model.co.PageCo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class SysNoticeCo extends PageCo {


    @ApiModelProperty(value = "公告名称")
    private String name;
}

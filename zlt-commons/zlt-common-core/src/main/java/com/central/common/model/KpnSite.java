package com.central.common.model;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("kpn_site")
@ApiModel("下载站")
public class KpnSite extends SuperEntity{

    @ApiModelProperty(value = "站点编码")
    private String code;

    @ApiModelProperty(value = "站点名称")
    private String name;

    @ApiModelProperty(value = "站点logo地址")
    private String logoUrl;

    @ApiModelProperty(value = "域名")
    private String domains;

    @ApiModelProperty(value = "站点状态 0关闭,1开启")
    private Boolean status;

    @ApiModelProperty(value = "站点维护状态 0未维护,1维护中")
    private Boolean repairStatus;

    @ApiModelProperty(value = "币种编码")
    private Boolean currency_code;

    @ApiModelProperty(value = "会员数")
    private Boolean memberNum;

    @ApiModelProperty(value = "VIP数")
    private Boolean vipNum;
}

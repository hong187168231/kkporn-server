package com.central.common.model;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/*
 * @Author: Lulu
 * @Date: 2023/2/24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("kpn_site_platform")
@ApiModel("站点平台配置")
public class KpnSitePlatform extends SuperEntity {
	@ApiModelProperty(value = "站点id")
	private int siteId;

	@ApiModelProperty(value = "站点编码")
	private String siteCode;

	@ApiModelProperty(value = "站点名称")
	private String siteName;

	@ApiModelProperty(value = "兑换K币")
	private BigDecimal exchange;


	@ApiModelProperty(value = "试看时长(秒)")
	private Integer tryTime;

	@ApiModelProperty(value = "防丢失域名")
	private String lostDomain;




}

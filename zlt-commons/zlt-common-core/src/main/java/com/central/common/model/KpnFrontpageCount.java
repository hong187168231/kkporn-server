package com.central.common.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.central.common.model.SuperEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 首页访问量统计
 *
 * @author yixiu
 * @date 2023-02-09 19:41:45
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("kpn_frontpage_count")
public class KpnFrontpageCount {
    private static final long serialVersionUID=1L;
    @TableId
    private Long id;
    @ApiModelProperty(value = "站点ID")
    private Long siteId;
    @ApiModelProperty(value = "访问量")
    private Long pvCount;
    @ApiModelProperty(value = "独立访客数")
    private Long uvCount;
    @ApiModelProperty(value = "统计日期")
    private String countDate;
    @ApiModelProperty(value = "充值单数")
    private Long rechargeNumber;
    @ApiModelProperty(value = "充值金额")
    private BigDecimal rechargeAmount;
    @ApiModelProperty(value = "每日新增会员人数")
    private Long addUsers;
    }

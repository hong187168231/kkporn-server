package com.central.common.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.central.common.model.SuperEntity;
import com.baomidou.mybatisplus.annotation.TableName;
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
        private Long pvCount;
        private Long uvCount;
        private String countDate;
        private Long onlineUsers;
        private Long rechargeNumber;
        private BigDecimal rechargeAmount;
        private Long addUsers;
    }

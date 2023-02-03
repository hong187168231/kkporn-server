package com.central.common.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 演员列表
 *
 * @author yixiu
 * @date 2023-02-03 16:31:09
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("kpn_actor")
public class KpnActor extends SuperEntity {
    private static final long serialVersionUID=1L;

        private String nameZh;
        private String nameEn;
        private String nameKh;
        private Integer sex;
        private String birthday;
        private String country;
        private BigDecimal height;
        private BigDecimal weight;
        private String bwh;
        private String avatarUrl;
        private String portraitUrl;
        private String interest;
        private String remark;
        private Date createTime;
        private Date updateTime;
        private String createBy;
        private String updateBy;
    }

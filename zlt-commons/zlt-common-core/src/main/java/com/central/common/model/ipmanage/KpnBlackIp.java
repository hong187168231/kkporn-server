package com.central.common.model.ipmanage;

import com.baomidou.mybatisplus.annotation.TableName;
import com.central.common.model.SuperEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import java.util.Date;

/**
 * 
 *
 * @author yixiu
 * @date 2023-02-03 15:50:11
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@TableName("kpn_black_ip")
public class KpnBlackIp extends SuperEntity {
    private static final long serialVersionUID=1L;

        private String ipSection;
        private String remark;
        private Date createTime;
        private Date updateTime;
        private String createBy;
        private String updateBy;
    }

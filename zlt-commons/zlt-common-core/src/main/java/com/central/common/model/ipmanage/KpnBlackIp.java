package com.central.common.model.ipmanage;

import com.baomidou.mybatisplus.annotation.TableName;
import com.central.common.model.SuperEntity;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "会员黑名单ip段")
    private String ipSection;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "创建人")
    private String createBy;
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    }

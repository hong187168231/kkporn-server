package com.central.common.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * @author zlt
 * 用户日志实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("login_log")
@ApiModel("用户日志实体")
public class LoginLog extends SuperEntity {

	@ApiModelProperty(value = "用户名")
	private String platName;

	@ApiModelProperty(value = "昵称")
	private String nickName;

	@ApiModelProperty(value = "用户id")
	private Long userId;

	@ApiModelProperty(value = "登录时间")
	private Date loginTime;

	@ApiModelProperty(value = "登录ip")
	private String loginIp;

	@ApiModelProperty(value = "登录国家")
	private String loginLocation;

	@ApiModelProperty(value = "设备")
	private String loginDevice;

	@ApiModelProperty(value = "厂商Code")
	private String merchantCode;

	@ApiModelProperty(value = "账号类型：APP：前端app用户，APP_GUEST：前端app游客用户，BACKEND：后端管理用户")
	private String type;

	@ApiModelProperty(value = "账号权限id(管理员)")
	private Long roleId;
}

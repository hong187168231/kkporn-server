package com.central.common.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author zlt
 * 公告实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_notice")
@ApiModel("公告实体")
public class SysNotice extends SuperEntity {
	private static final long serialVersionUID = -5886012896705137070L;

	@ApiModelProperty(value = "公告名称")
	private String name;

	@ApiModelProperty(value = "标题")
	private String title;
	@ApiModelProperty(value = "内容")
	private String content;

	@ApiModelProperty(value = "状态(0:停用,1:启用)")
	private Integer state;

	@ApiModelProperty(value = "状态按钮是否禁用")
	@TableField(exist = false)
	private Boolean stateType=false;


	@ApiModelProperty(value = "备注")
	private String remark;


	private String startTime;

	private String endTime;

	private String updateBy;

	private String createBy;

}

package com.smart.modules.mjPrompt.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.core.mp.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.time.LocalDateTime;


/**
 * 实体类
 *
 * @author SmartX
 */
@Data
@TableName("smart_mj_app")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MjApp对象", description = "")
public class MjAppEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;
	/**
	 * 应用名称
	 */
	@ApiModelProperty(value = "应用名称")
	private String appName;
	/**
	 * 应用code
	 */
	@ApiModelProperty(value = "应用code")
	private String appCode;

	/**
	 * linkAiCode
	 */
	@ApiModelProperty(value = "linkAiCode")
	private String linkAiCode;
	/**
	 * linkAiToken
	 */
	@ApiModelProperty(value = "linkAiToken")
	private String linkAiToken;
}

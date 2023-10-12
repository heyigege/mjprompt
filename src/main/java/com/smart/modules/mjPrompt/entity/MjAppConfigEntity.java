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
@TableName("smart_mj_app_config")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MjAppConfig对象", description = "")
public class MjAppConfigEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;
	/**
	 * 邮件配置json
	 */
	@ApiModelProperty(value = "邮件配置json")
	private String emailConfig;
	/**
	 * 保存路径
	 */
	@ApiModelProperty(value = "保存路径")
	private String folder;
	/**
	 * mj_app_id
	 */
	@ApiModelProperty(value = "mj_app_id")
	private Long appId;

}

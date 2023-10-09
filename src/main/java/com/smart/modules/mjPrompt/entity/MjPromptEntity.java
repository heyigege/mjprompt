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
 *  实体类
 *
 * @author SmartX
 */
@Data
@TableName("smart_mj_prompt")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MjPrompt对象", description = "")
public class MjPromptEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;
	/**
	 * prompt
	 */
	@ApiModelProperty(value = "prompt")
	private String prompt;
	/**
	 * Prompt拼接
	 */
	@ApiModelProperty(value = "Prompt拼接")
	private String phraseSuffix;
	/**
	 * appId
	 */
	@ApiModelProperty(value = "appId")
	private Long appId;
	/**
	 * 词组Id
	 */
	@ApiModelProperty(value = "词组Id")
	private Long phraseId;
	/**
	 * 拼接的Prompt
	 */
	@ApiModelProperty(value = "拼接的Prompt")
	private String promptOrigin;

}

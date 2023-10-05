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
@TableName("smart_mj_phrase")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MjPhrase对象", description = "")
public class MjPhraseEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;
	/**
	 * 名称
	 */
	@ApiModelProperty(value = "名称")
	private String phraseName;
	/**
	 * 词组尾缀
	 */
	@ApiModelProperty(value = "词组尾缀")
	private String phraseSuffix;
	/**
	 * mj_app_id
	 */
	@ApiModelProperty(value = "mj_app_id")
	private Long appId;

}

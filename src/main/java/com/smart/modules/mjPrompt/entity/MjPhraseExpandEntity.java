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
@TableName("smart_mj_phrase_expand")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MjPhraseExpand对象", description = "")
public class MjPhraseExpandEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;
	/**
	 * mj词组id
	 */
	@ApiModelProperty(value = "mj词组id")
	private Long phraseId;
	/**
	 * 类型id
	 */
	@ApiModelProperty(value = "类型id")
	private Long mainId;
	/**
	 * 词组id
	 */
	@ApiModelProperty(value = "词组id")
	private String dictbizId;
	/**
	 * 最少数量
	 */
	@ApiModelProperty(value = "最少数量")
	private Integer minNum;
	/**
	 * 最多数量
	 */
	@ApiModelProperty(value = "最多数量")
	private Integer maxNum;

}

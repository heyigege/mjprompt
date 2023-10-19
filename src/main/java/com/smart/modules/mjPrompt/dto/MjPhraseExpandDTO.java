package com.smart.modules.mjPrompt.dto;

import com.smart.modules.mjPrompt.entity.MjPhraseExpandEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 模型DTO
 *
 * @author SmartX
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MjPhraseExpandDTO extends MjPhraseExpandEntity {

	private static final long serialVersionUID = 1L;

	private String dictbizString;

}

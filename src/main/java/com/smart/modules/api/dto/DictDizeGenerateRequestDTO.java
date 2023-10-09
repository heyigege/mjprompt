package com.smart.modules.api.dto;

import lombok.Data;

@Data

public class DictDizeGenerateRequestDTO {
	private String prompt;
	private int num;
	private Long parentId;
}

package com.smart.modules.resource.vo;

import com.smart.core.oss.model.SmartFile;
import lombok.Data;

@Data
public class AttachSmartFileVo extends SmartFile {
	private static final long serialVersionUID = 2L;
	private String md5;

	private Long size;
}

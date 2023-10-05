/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package com.smart.modules.resource.builder.oss;

import com.smart.modules.resource.entity.Oss;
import io.minio.MinioClient;
import lombok.SneakyThrows;
import com.smart.core.oss.OssTemplate;
import com.smart.core.oss.MinioTemplate;
import com.smart.core.oss.props.OssProperties;
import com.smart.core.oss.rule.OssRule;

/**
 * Minio云存储构建类
 *
 * @author Chill
 */
public class MinioOssBuilder {

	@SneakyThrows
	public static OssTemplate template(Oss oss, OssRule ossRule) {
		// 创建配置类
		OssProperties ossProperties = new OssProperties();
		ossProperties.setEndpoint(oss.getEndpoint());
		ossProperties.setAccessKey(oss.getAccessKey());
		ossProperties.setSecretKey(oss.getSecretKey());
		ossProperties.setBucketName(oss.getBucketName());
		// 创建客户端
		MinioClient minioClient = MinioClient.builder()
			.endpoint(oss.getEndpoint())
			.credentials(oss.getAccessKey(), oss.getSecretKey())
			.build();
		return new MinioTemplate(minioClient, ossRule, ossProperties);
	}

}

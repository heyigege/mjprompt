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
package com.smart.modules.resource.config;

import lombok.AllArgsConstructor;
import com.smart.core.redis.cache.SmartRedis;
import com.smart.core.sms.props.SmsProperties;
import com.smart.modules.resource.builder.sms.SmsBuilder;
import com.smart.modules.resource.service.ISmsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sms配置类
 *
 * @author Chill
 */
@Configuration
@AllArgsConstructor
public class SmartSmsConfiguration {

	private final SmsProperties smsProperties;

	private final ISmsService smsService;

	private final SmartRedis smartRedis;

	@Bean
	public SmsBuilder smsBuilder() {
		return new SmsBuilder(smsProperties, smsService, smartRedis);
	}

}

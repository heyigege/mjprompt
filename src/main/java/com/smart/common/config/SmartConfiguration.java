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
package com.smart.common.config;


import com.smart.core.secure.registry.SecureRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Smart配置
 *
 * @author Chill
 */
@Configuration
public class SmartConfiguration implements WebMvcConfigurer {

	/**
	 * 安全框架配置
	 */
	@Bean
	public SecureRegistry secureRegistry() {
		SecureRegistry secureRegistry = new SecureRegistry();
		secureRegistry.setEnabled(true);
		secureRegistry.excludePathPatterns("/smart-auth/**");
		secureRegistry.excludePathPatterns("/smart-system/menu/routes");
		secureRegistry.excludePathPatterns("/smart-system/menu/auth-routes");
		secureRegistry.excludePathPatterns("/smart-system/menu/top-menu");
		secureRegistry.excludePathPatterns("/smart-system/tenant/info");
		secureRegistry.excludePathPatterns("/smart-flow/process/resource-view");
		secureRegistry.excludePathPatterns("/smart-flow/process/diagram-view");
		secureRegistry.excludePathPatterns("/smart-flow/manager/check-upload");
		secureRegistry.excludePathPatterns("/doc.html");
		secureRegistry.excludePathPatterns("/js/**");
		secureRegistry.excludePathPatterns("/webjars/**");
		secureRegistry.excludePathPatterns("/swagger-resources/**");
		secureRegistry.excludePathPatterns("/druid/**");
		return secureRegistry;
	}

	/**
	 * 跨域配置
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/cors/**")
			.allowedOrigins("*")
			.allowedHeaders("*")
			.allowedMethods("*")
			.maxAge(3600)
			.allowCredentials(true);
	}

}

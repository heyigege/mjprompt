package com.smart.common.config;

import com.smart.common.filter.PreviewFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 演示配置类
 *
 * @author Chill
 */
@Configuration
@ConditionalOnProperty(value = "smart.preview.enabled", havingValue = "true")
public class SmartPreviewConfiguration {

	/**
	 * 演示模式配置
	 */
	@Bean
	public PreviewFilter previewFilter() {
		return new PreviewFilter();
	}


}

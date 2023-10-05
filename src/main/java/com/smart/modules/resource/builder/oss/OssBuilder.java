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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.smart.modules.resource.entity.Oss;
import com.smart.core.cache.utils.CacheUtil;
import com.smart.core.log.exception.ServiceException;
import com.smart.core.oss.OssTemplate;
import com.smart.core.oss.enums.OssEnum;
import com.smart.core.oss.enums.OssStatusEnum;
import com.smart.core.oss.props.OssProperties;
import com.smart.core.oss.rule.SmartOssRule;
import com.smart.core.oss.rule.OssRule;
import com.smart.core.secure.utils.AuthUtil;
import com.smart.core.tool.utils.Func;
import com.smart.core.tool.utils.StringPool;
import com.smart.core.tool.utils.StringUtil;
import com.smart.core.tool.utils.WebUtil;
import com.smart.modules.resource.service.IOssService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.smart.core.cache.constant.CacheConstant.RESOURCE_CACHE;

/**
 * Oss云存储统一构建类
 *
 * @author Chill
 */
public class OssBuilder {

	public static final String OSS_CODE = "oss:code:";
	public static final String OSS_PARAM_KEY = "code";

	private final OssProperties ossProperties;
	private final IOssService ossService;

	public OssBuilder(OssProperties ossProperties, IOssService ossService) {
		this.ossProperties = ossProperties;
		this.ossService = ossService;
	}

	/**
	 * OssTemplate配置缓存池
	 */
	private final Map<String, OssTemplate> templatePool = new ConcurrentHashMap<>();

	/**
	 * oss配置缓存池
	 */
	private final Map<String, Oss> ossPool = new ConcurrentHashMap<>();

	/**
	 * 获取template
	 *
	 * @return OssTemplate
	 */
	public OssTemplate template() {
		return template(StringPool.EMPTY);
	}

	/**
	 * 获取template
	 *
	 * @param code 资源编号
	 * @return OssTemplate
	 */
	public OssTemplate template(String code) {
		String tenantId = AuthUtil.getTenantId();
		Oss oss = getOss(tenantId, code);
		Oss ossCached = ossPool.get(tenantId);
		OssTemplate template = templatePool.get(tenantId);
		// 若为空或者不一致，则重新加载
		if (Func.hasEmpty(template, ossCached) || !oss.getEndpoint().equals(ossCached.getEndpoint()) || !oss.getAccessKey().equals(ossCached.getAccessKey())) {
			synchronized (OssBuilder.class) {
				template = templatePool.get(tenantId);
				if (Func.hasEmpty(template, ossCached) || !oss.getEndpoint().equals(ossCached.getEndpoint()) || !oss.getAccessKey().equals(ossCached.getAccessKey())) {
					OssRule ossRule;
					// 若采用默认设置则开启多租户模式, 若是用户自定义oss则不开启
					if (oss.getEndpoint().equals(ossProperties.getEndpoint()) && oss.getAccessKey().equals(ossProperties.getAccessKey()) && ossProperties.getTenantMode()) {
						ossRule = new SmartOssRule(Boolean.TRUE);
					} else {
						ossRule = new SmartOssRule(Boolean.FALSE);
					}
					if (oss.getCategory() == OssEnum.MINIO.getCategory()) {
						template = MinioOssBuilder.template(oss, ossRule);
					} else if (oss.getCategory() == OssEnum.QINIU.getCategory()) {
						template = QiniuOssBuilder.template(oss, ossRule);
					} else if (oss.getCategory() == OssEnum.ALI.getCategory()) {
						template = AliOssBuilder.template(oss, ossRule);
					} else if (oss.getCategory() == OssEnum.TENCENT.getCategory()) {
						template = TencentOssBuilder.template(oss, ossRule);
					}
					templatePool.put(tenantId, template);
					ossPool.put(tenantId, oss);
				}
			}
		}
		return template;
	}

	/**
	 * 获取对象存储实体
	 *
	 * @param tenantId 租户ID
	 * @return Oss
	 */
	public Oss getOss(String tenantId, String code) {
		String key = tenantId;
		LambdaQueryWrapper<Oss> lqw = Wrappers.<Oss>query().lambda().eq(Oss::getTenantId, tenantId);
		// 获取传参的资源编号并查询，若有则返回，若没有则调启用的配置
		String ossCode = StringUtil.isBlank(code) ? WebUtil.getParameter(OSS_PARAM_KEY) : code;
		if (StringUtil.isNotBlank(ossCode)) {
			key = key.concat(StringPool.DASH).concat(ossCode);
			lqw.eq(Oss::getOssCode, ossCode);
		} else {
			lqw.eq(Oss::getStatus, OssStatusEnum.ENABLE.getNum());
		}
		Oss oss = CacheUtil.get(RESOURCE_CACHE, OSS_CODE, key, () -> {
			Oss o = ossService.getOne(lqw);
			// 若为空则调用默认配置
			if ((Func.isEmpty(o))) {
				Oss defaultOss = new Oss();
				defaultOss.setId(0L);
				defaultOss.setCategory(OssEnum.of(ossProperties.getName()).getCategory());
				defaultOss.setEndpoint(ossProperties.getEndpoint());
				defaultOss.setBucketName(ossProperties.getBucketName());
				defaultOss.setAccessKey(ossProperties.getAccessKey());
				defaultOss.setSecretKey(ossProperties.getSecretKey());
				return defaultOss;
			} else {
				return o;
			}
		});
		if (oss == null || oss.getId() == null) {
			throw new ServiceException("未获取到对应的对象存储配置");
		} else {
			return oss;
		}
	}

}

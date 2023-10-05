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
package com.smart.modules.system.wrapper;

import com.smart.common.cache.DictCache;
import com.smart.common.enums.DictEnum;
import com.smart.modules.system.entity.ApiScope;
import com.smart.modules.system.vo.ApiScopeVO;
import com.smart.core.mp.support.BaseEntityWrapper;
import com.smart.core.tool.utils.BeanUtil;

import java.util.Objects;


/**
 * 包装类,返回视图层所需的字段
 *
 * @author Chill
 */
public class ApiScopeWrapper extends BaseEntityWrapper<ApiScope, ApiScopeVO> {

	public static ApiScopeWrapper build() {
		return new ApiScopeWrapper();
	}

	@Override
	public ApiScopeVO entityVO(ApiScope dataScope) {
		ApiScopeVO apiScopeVO = Objects.requireNonNull(BeanUtil.copy(dataScope, ApiScopeVO.class));
		String scopeTypeName = DictCache.getValue(DictEnum.API_SCOPE_TYPE, dataScope.getScopeType());
		apiScopeVO.setScopeTypeName(scopeTypeName);
		return apiScopeVO;
	}

}

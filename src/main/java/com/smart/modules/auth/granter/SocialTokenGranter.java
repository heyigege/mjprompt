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
package com.smart.modules.auth.granter;

import com.smart.modules.auth.utils.TokenUtil;
import lombok.AllArgsConstructor;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import com.smart.core.log.exception.ServiceException;
import com.smart.core.social.props.SocialProperties;
import com.smart.core.social.utils.SocialUtil;
import com.smart.core.tool.utils.BeanUtil;
import com.smart.core.tool.utils.Func;
import com.smart.core.tool.utils.WebUtil;
import com.smart.modules.auth.provider.ITokenGranter;
import com.smart.modules.auth.provider.TokenParameter;
import com.smart.modules.system.entity.UserInfo;
import com.smart.modules.system.entity.UserOauth;
import com.smart.modules.system.service.IUserService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * SocialTokenGranter
 *
 * @author Chill
 */
@Component
@AllArgsConstructor
public class SocialTokenGranter implements ITokenGranter {

	public static final String GRANT_TYPE = "social";

	private static final Integer AUTH_SUCCESS_CODE = 2000;

	private final IUserService userService;
	private final SocialProperties socialProperties;

	@Override
	public UserInfo grant(TokenParameter tokenParameter) {
		HttpServletRequest request = WebUtil.getRequest();
		String tenantId = Func.toStr(request.getHeader(TokenUtil.TENANT_HEADER_KEY), TokenUtil.DEFAULT_TENANT_ID);
		// 开放平台来源
		String sourceParameter = request.getParameter("source");
		// 匹配是否有别名定义
		String source = socialProperties.getAlias().getOrDefault(sourceParameter, sourceParameter);
		// 开放平台授权码
		String code = request.getParameter("code");
		// 开放平台状态吗
		String state = request.getParameter("state");

		// 获取开放平台授权数据
		AuthRequest authRequest = SocialUtil.getAuthRequest(source, socialProperties);
		AuthCallback authCallback = new AuthCallback();
		authCallback.setCode(code);
		authCallback.setState(state);
		AuthResponse authResponse = authRequest.login(authCallback);
		AuthUser authUser;
		if (authResponse.getCode() == AUTH_SUCCESS_CODE) {
			authUser = (AuthUser) authResponse.getData();
		} else {
			throw new ServiceException("social grant failure, auth response is not success");
		}

		// 组装数据
		UserOauth userOauth = Objects.requireNonNull(BeanUtil.copy(authUser, UserOauth.class));
		userOauth.setSource(authUser.getSource());
		userOauth.setTenantId(tenantId);
		userOauth.setUuid(authUser.getUuid());
		// 返回UserInfo
		return userService.userInfo(userOauth);
	}

}

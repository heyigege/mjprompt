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

import com.smart.common.cache.CacheNames;
import com.smart.common.cache.ParamCache;
import com.smart.modules.auth.enums.UserEnum;
import lombok.AllArgsConstructor;
import com.smart.core.log.exception.ServiceException;
import com.smart.core.redis.cache.SmartRedis;
import com.smart.core.tool.utils.DigestUtil;
import com.smart.core.tool.utils.Func;
import com.smart.core.tool.utils.WebUtil;
import com.smart.modules.auth.provider.ITokenGranter;
import com.smart.modules.auth.provider.TokenParameter;
import com.smart.modules.auth.utils.TokenUtil;
import com.smart.modules.system.entity.Tenant;
import com.smart.modules.system.entity.UserInfo;
import com.smart.modules.system.service.IRoleService;
import com.smart.modules.system.service.ITenantService;
import com.smart.modules.system.service.IUserService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.List;

/**
 * PasswordTokenGranter
 *
 * @author Chill
 */
@Component
@AllArgsConstructor
public class PasswordTokenGranter implements ITokenGranter {

	public static final String GRANT_TYPE = "password";
	public static final Integer FAIL_COUNT = 5;
	public static final String FAIL_COUNT_VALUE = "account.failCount";

	private final IUserService userService;
	private final IRoleService roleService;
	private final ITenantService tenantService;
	private final SmartRedis smartRedis;

	@Override
	public UserInfo grant(TokenParameter tokenParameter) {
		HttpServletRequest request = WebUtil.getRequest();
		// 获取用户绑定ID
		String headerDept = request.getHeader(TokenUtil.DEPT_HEADER_KEY);
		String headerRole = request.getHeader(TokenUtil.ROLE_HEADER_KEY);

		// 获取用户信息
		String tenantId = tokenParameter.getArgs().getStr("tenantId");
		String username = tokenParameter.getArgs().getStr("username");
		String password = tokenParameter.getArgs().getStr("password");

		// 判断登录是否锁定
		int cnt = Func.toInt(smartRedis.get(CacheNames.tenantKey(tenantId, CacheNames.USER_FAIL_KEY, username)), 0);
		int failCount = Func.toInt(ParamCache.getValue(FAIL_COUNT_VALUE), FAIL_COUNT);
		if (cnt >= failCount) {
			throw new ServiceException(TokenUtil.USER_HAS_TOO_MANY_FAILS);
		}

		UserInfo userInfo = null;
		if (Func.isNoneBlank(username, password)) {
			// 获取租户信息
			Tenant tenant = tenantService.getByTenantId(tenantId);
			if (TokenUtil.judgeTenant(tenant)) {
				throw new ServiceException(TokenUtil.USER_HAS_NO_TENANT_PERMISSION);
			}
			// 获取用户类型
			String userType = tokenParameter.getArgs().getStr("userType");
			// 根据不同用户类型调用对应的接口返回数据，用户可自行拓展
			if (userType.equals(UserEnum.WEB.getName())) {
				userInfo = userService.userInfo(tenantId, username, DigestUtil.hex(password), UserEnum.WEB);
			} else if (userType.equals(UserEnum.APP.getName())) {
				userInfo = userService.userInfo(tenantId, username, DigestUtil.hex(password), UserEnum.APP);
			} else {
				userInfo = userService.userInfo(tenantId, username, DigestUtil.hex(password), UserEnum.OTHER);
			}
		}
		// 错误次数锁定
		if (userInfo == null || userInfo.getUser() == null) {
			smartRedis.setEx(CacheNames.tenantKey(tenantId, CacheNames.USER_FAIL_KEY, username), cnt + 1, Duration.ofMinutes(30));
		}
		// 多部门情况下指定单部门
		if (Func.isNotEmpty(headerDept) && userInfo != null && userInfo.getUser().getDeptId().contains(headerDept)) {
			userInfo.getUser().setDeptId(headerDept);
		}
		// 多角色情况下指定单角色
		if (Func.isNotEmpty(headerRole) && userInfo != null && userInfo.getUser().getRoleId().contains(headerRole)) {
			List<String> roleAliases = roleService.getRoleAliases(headerRole);
			userInfo.setRoles(roleAliases);
			userInfo.getUser().setRoleId(headerRole);
		}
		// 成功则清除登录错误次数
		smartRedis.del(CacheNames.tenantKey(tenantId, CacheNames.USER_FAIL_KEY, username));
		return userInfo;
	}

}

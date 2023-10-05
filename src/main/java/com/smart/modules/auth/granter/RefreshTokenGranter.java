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
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import com.smart.core.launch.constant.TokenConstant;
import com.smart.core.log.exception.ServiceException;
import com.smart.core.secure.utils.AuthUtil;
import com.smart.core.tool.utils.Func;
import com.smart.modules.auth.provider.ITokenGranter;
import com.smart.modules.auth.provider.TokenParameter;
import com.smart.modules.system.entity.Tenant;
import com.smart.modules.system.entity.UserInfo;
import com.smart.modules.system.service.IRoleService;
import com.smart.modules.system.service.ITenantService;
import com.smart.modules.system.service.IUserService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * RefreshTokenGranter
 *
 * @author Chill
 */
@Component
@AllArgsConstructor
public class RefreshTokenGranter implements ITokenGranter {

	public static final String GRANT_TYPE = "refresh_token";

	private final IUserService userService;
	private final IRoleService roleService;
	private final ITenantService tenantService;

	@Override
	public UserInfo grant(TokenParameter tokenParameter) {
		String tenantId = tokenParameter.getArgs().getStr("tenantId");
		String grantType = tokenParameter.getArgs().getStr("grantType");
		String refreshToken = tokenParameter.getArgs().getStr("refreshToken");
		String deptId = tokenParameter.getArgs().getStr("deptId");
		String roleId = tokenParameter.getArgs().getStr("roleId");
		UserInfo userInfo = null;
		if (Func.isNoneBlank(grantType, refreshToken) && grantType.equals(TokenConstant.REFRESH_TOKEN)) {
			Claims claims = AuthUtil.parseJWT(refreshToken);
			if (claims != null) {
				String tokenType = Func.toStr(claims.get(TokenConstant.TOKEN_TYPE));
				if (tokenType.equals(TokenConstant.REFRESH_TOKEN)) {
					// 获取租户信息
					Tenant tenant = tenantService.getByTenantId(tenantId);
					if (TokenUtil.judgeTenant(tenant)) {
						throw new ServiceException(TokenUtil.USER_HAS_NO_TENANT_PERMISSION);
					}
					// 获取用户信息
					userInfo = userService.userInfo(Func.toLong(claims.get(TokenConstant.USER_ID)));
					// 设置多部门信息
					if (Func.isNotEmpty(deptId) && userInfo.getUser().getDeptId().contains(deptId)) {
						userInfo.getUser().setDeptId(deptId);
					}
					// 设置多角色信息
					if (Func.isNotEmpty(roleId) && userInfo.getUser().getRoleId().contains(roleId)) {
						userInfo.getUser().setRoleId(roleId);
						List<String> roleAliases = roleService.getRoleAliases(roleId);
						userInfo.setRoles(roleAliases);
					}
				}
			}
		}
		return userInfo;
	}
}

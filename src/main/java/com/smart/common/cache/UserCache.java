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
package com.smart.common.cache;

import com.smart.core.cache.utils.CacheUtil;
import com.smart.core.tool.utils.Func;
import com.smart.core.tool.utils.SpringUtil;
import com.smart.core.tool.utils.StringPool;
import com.smart.core.tool.utils.StringUtil;
import com.smart.modules.system.entity.User;
import com.smart.modules.system.service.IUserService;

import static com.smart.core.cache.constant.CacheConstant.USER_CACHE;
import static com.smart.core.launch.constant.FlowConstant.TASK_USR_PREFIX;

/**
 * 系统缓存
 *
 * @author Chill
 */
public class UserCache {
	private static final String USER_CACHE_ID = "user:id:";
	private static final String USER_CACHE_ACCOUNT = "user:account:";

	private static final IUserService userService;

	static {
		userService = SpringUtil.getBean(IUserService.class);
	}

	/**
	 * 根据任务用户id获取用户信息
	 *
	 * @param taskUserId 任务用户id
	 * @return
	 */
	public static User getUserByTaskUser(String taskUserId) {
		Long userId = Func.toLong(StringUtil.removePrefix(taskUserId, TASK_USR_PREFIX));
		return getUser(userId);
	}

	/**
	 * 获取用户
	 *
	 * @param userId 用户id
	 * @return
	 */
	public static User getUser(Long userId) {
		return CacheUtil.get(USER_CACHE, USER_CACHE_ID, userId, () -> userService.getById(userId));
	}

	/**
	 * 获取用户
	 *
	 * @param tenantId 租户id
	 * @param account  账号名
	 * @return
	 */
	public static User getUser(String tenantId, String account) {
		return CacheUtil.get(USER_CACHE, USER_CACHE_ACCOUNT, tenantId + StringPool.DASH + account, () -> userService.userByAccount(tenantId, account));
	}

}

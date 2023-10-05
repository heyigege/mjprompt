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

import com.smart.common.cache.SysCache;
import com.smart.core.mp.support.BaseEntityWrapper;
import com.smart.core.tool.constant.SmartConstant;
import com.smart.core.tool.node.ForestNodeMerger;
import com.smart.core.tool.utils.BeanUtil;
import com.smart.core.tool.utils.Func;
import com.smart.modules.system.entity.Role;
import com.smart.modules.system.vo.RoleVO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 包装类,返回视图层所需的字段
 *
 * @author Chill
 */
public class RoleWrapper extends BaseEntityWrapper<Role, RoleVO> {

	public static RoleWrapper build() {
		return new RoleWrapper();
	}

	@Override
	public RoleVO entityVO(Role role) {
		RoleVO roleVO = Objects.requireNonNull(BeanUtil.copy(role, RoleVO.class));
		if (Func.equals(role.getParentId(), SmartConstant.TOP_PARENT_ID)) {
			roleVO.setParentName(SmartConstant.TOP_PARENT_NAME);
		} else {
			Role parent = SysCache.getRole(role.getParentId());
			roleVO.setParentName(parent.getRoleName());
		}
		return roleVO;
	}


	public List<RoleVO> listNodeVO(List<Role> list) {
		List<RoleVO> collect = list.stream().map(this::entityVO).collect(Collectors.toList());
		return ForestNodeMerger.merge(collect);
	}

}

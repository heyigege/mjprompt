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
package com.smart.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.common.constant.CommonConstant;
import com.smart.modules.system.entity.Role;
import com.smart.modules.system.entity.RoleMenu;
import com.smart.modules.system.entity.RoleScope;
import com.smart.modules.system.mapper.RoleMapper;
import com.smart.modules.system.service.IRoleMenuService;
import com.smart.modules.system.service.IRoleScopeService;
import com.smart.modules.system.service.IRoleService;
import com.smart.modules.system.vo.RoleVO;
import com.smart.modules.system.wrapper.RoleWrapper;
import lombok.AllArgsConstructor;
import com.smart.core.log.exception.ServiceException;
import com.smart.core.secure.utils.AuthUtil;
import com.smart.core.tool.constant.SmartConstant;
import com.smart.core.tool.constant.RoleConstant;
import com.smart.core.tool.node.ForestNodeMerger;
import com.smart.core.tool.utils.CollectionUtil;
import com.smart.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
@Validated
@AllArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

	private final IRoleMenuService roleMenuService;
	private final IRoleScopeService roleScopeService;

	@Override
	public IPage<RoleVO> selectRolePage(IPage<RoleVO> page, RoleVO role) {
		return page.setRecords(baseMapper.selectRolePage(page, role));
	}

	@Override
	public List<RoleVO> tree(String tenantId) {
		String userRole = AuthUtil.getUserRole();
		String excludeRole = null;
		if (!CollectionUtil.contains(Func.toStrArray(userRole), RoleConstant.ADMIN) && !CollectionUtil.contains(Func.toStrArray(userRole), RoleConstant.ADMINISTRATOR)) {
			excludeRole = RoleConstant.ADMIN;
		}
		return ForestNodeMerger.merge(baseMapper.tree(tenantId, excludeRole));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean grant(@NotEmpty List<Long> roleIds, List<Long> menuIds, List<Long> dataScopeIds, List<Long> apiScopeIds) {
		return grantRoleMenu(roleIds, menuIds) && grantDataScope(roleIds, dataScopeIds) && grantApiScope(roleIds, apiScopeIds);
	}

	private boolean grantRoleMenu(List<Long> roleIds, List<Long> menuIds) {
		// 防止越权配置超管角色
		int administratorCount = baseMapper.selectCount(Wrappers.<Role>query().lambda().eq(Role::getRoleAlias, RoleConstant.ADMINISTRATOR).in(Role::getId, roleIds));
		if (!AuthUtil.isAdministrator() && administratorCount > 0) {
			throw new ServiceException("无权配置超管角色!");
		}
		// 防止越权配置管理员角色
		int adminCount = baseMapper.selectCount(Wrappers.<Role>query().lambda().eq(Role::getRoleAlias, RoleConstant.ADMIN).in(Role::getId, roleIds));
		if (!AuthUtil.isAdmin() && adminCount > 0) {
			throw new ServiceException("无权配置管理员角色!");
		}
		// 删除角色配置的菜单集合
		roleMenuService.remove(Wrappers.<RoleMenu>update().lambda().in(RoleMenu::getRoleId, roleIds));
		// 组装配置
		List<RoleMenu> roleMenus = new ArrayList<>();
		roleIds.forEach(roleId -> menuIds.forEach(menuId -> {
			RoleMenu roleMenu = new RoleMenu();
			roleMenu.setRoleId(roleId);
			roleMenu.setMenuId(menuId);
			roleMenus.add(roleMenu);
		}));
		// 新增配置
		roleMenuService.saveBatch(roleMenus);
		// 递归设置下属角色菜单集合
		recursionRoleMenu(roleIds, menuIds);
		return true;
	}

	private void recursionRoleMenu(List<Long> roleIds, List<Long> menuIds) {
		roleIds.forEach(roleId -> baseMapper.selectList(Wrappers.<Role>query().lambda().eq(Role::getParentId, roleId)).forEach(role -> {
			List<RoleMenu> roleMenuList = roleMenuService.list(Wrappers.<RoleMenu>query().lambda().eq(RoleMenu::getRoleId, role.getId()));
			// 子节点过滤出父节点删除的菜单集合
			List<Long> collectRoleMenuIds = roleMenuList.stream().map(RoleMenu::getMenuId).filter(menuId -> !menuIds.contains(menuId)).collect(Collectors.toList());
			if (collectRoleMenuIds.size() > 0) {
				// 删除子节点权限外的菜单集合
				roleMenuService.remove(Wrappers.<RoleMenu>update().lambda().eq(RoleMenu::getRoleId, role.getId()).in(RoleMenu::getMenuId, collectRoleMenuIds));
				// 递归设置下属角色菜单集合
				recursionRoleMenu(Collections.singletonList(role.getId()), menuIds);
			}
		}));
	}

	private boolean grantDataScope(List<Long> roleIds, List<Long> dataScopeIds) {
		// 删除角色配置的数据权限集合
		roleScopeService.remove(Wrappers.<RoleScope>update().lambda().eq(RoleScope::getScopeCategory, CommonConstant.DATA_SCOPE_CATEGORY).in(RoleScope::getRoleId, roleIds));
		// 组装配置
		List<RoleScope> roleDataScopes = new ArrayList<>();
		roleIds.forEach(roleId -> dataScopeIds.forEach(scopeId -> {
			RoleScope roleScope = new RoleScope();
			roleScope.setScopeCategory(CommonConstant.DATA_SCOPE_CATEGORY);
			roleScope.setRoleId(roleId);
			roleScope.setScopeId(scopeId);
			roleDataScopes.add(roleScope);
		}));
		// 新增配置
		roleScopeService.saveBatch(roleDataScopes);
		return true;
	}

	private boolean grantApiScope(List<Long> roleIds, List<Long> apiScopeIds) {
		// 删除角色配置的接口权限集合
		roleScopeService.remove(Wrappers.<RoleScope>update().lambda().eq(RoleScope::getScopeCategory, CommonConstant.API_SCOPE_CATEGORY).in(RoleScope::getRoleId, roleIds));
		// 组装配置
		List<RoleScope> roleApiScopes = new ArrayList<>();
		roleIds.forEach(roleId -> apiScopeIds.forEach(scopeId -> {
			RoleScope roleScope = new RoleScope();
			roleScope.setScopeCategory(CommonConstant.API_SCOPE_CATEGORY);
			roleScope.setScopeId(scopeId);
			roleScope.setRoleId(roleId);
			roleApiScopes.add(roleScope);
		}));
		// 新增配置
		roleScopeService.saveBatch(roleApiScopes);
		return true;
	}

	@Override
	public String getRoleIds(String tenantId, String roleNames) {
		List<Role> roleList = baseMapper.selectList(Wrappers.<Role>query().lambda().eq(Role::getTenantId, tenantId).in(Role::getRoleName, Func.toStrList(roleNames)));
		if (roleList != null && roleList.size() > 0) {
			return roleList.stream().map(role -> Func.toStr(role.getId())).distinct().collect(Collectors.joining(","));
		}
		return null;
	}

	@Override
	public List<String> getRoleNames(String roleIds) {
		return baseMapper.getRoleNames(Func.toLongArray(roleIds));
	}

	@Override
	public List<String> getRoleAliases(String roleIds) {
		return baseMapper.getRoleAliases(Func.toLongArray(roleIds));
	}

	@Override
	public boolean submit(Role role) {
		if (!AuthUtil.isAdministrator()) {
			if (Func.toStr(role.getRoleAlias()).equals(RoleConstant.ADMINISTRATOR)) {
				throw new ServiceException("无权限创建超管角色！");
			}
		}
		if (Func.isEmpty(role.getParentId())) {
			role.setTenantId(AuthUtil.getTenantId());
			role.setParentId(SmartConstant.TOP_PARENT_ID);
		}
		if (role.getParentId() > 0) {
			Role parent = getById(role.getParentId());
			if (Func.toLong(role.getParentId()) == Func.toLong(role.getId())) {
				throw new ServiceException("父节点不可选择自身!");
			}
			role.setTenantId(parent.getTenantId());
		}
		role.setIsDeleted(SmartConstant.DB_NOT_DELETED);
		return saveOrUpdate(role);
	}

	@Override
	public List<RoleVO> search(String roleName, Long parentId) {
		String tenantId = AuthUtil.getTenantId();
		LambdaQueryWrapper<Role> queryWrapper = Wrappers.<Role>query().lambda();
		if (Func.isNotEmpty(roleName)) {
			queryWrapper.like(Role::getRoleName, roleName);
		}
		if (Func.isNotEmpty(parentId) && parentId > 0L) {
			queryWrapper.eq(Role::getParentId, parentId);
		}
		if (Func.isNotEmpty(tenantId)) {
			queryWrapper.eq(Role::getTenantId, tenantId);
		}
		List<Role> roleList = baseMapper.selectList(queryWrapper);
		return RoleWrapper.build().listNodeVO(roleList);
	}

	@Override
	public boolean removeRole(String ids) {
		Integer cnt = baseMapper.selectCount(Wrappers.<Role>query().lambda().in(Role::getParentId, Func.toLongList(ids)));
		if (cnt > 0) {
			throw new ServiceException("请先删除子节点!");
		}
		return removeByIds(Func.toLongList(ids));
	}

}

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
package com.smart.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smart.modules.system.dto.MenuDTO;
import com.smart.modules.system.entity.Menu;
import com.smart.modules.system.vo.MenuVO;
import com.smart.core.tool.node.TreeNode;

import java.util.List;
import java.util.Map;

/**
 * Mapper 接口
 *
 * @author Chill
 */
public interface MenuMapper extends BaseMapper<Menu> {

	/**
	 * 懒加载列表
	 *
	 * @param parentId
	 * @param param
	 * @return
	 */
	List<MenuVO> lazyList(Long parentId, Map<String, Object> param);

	/**
	 * 懒加载菜单列表
	 *
	 * @param parentId
	 * @param param
	 * @return
	 */
	List<MenuVO> lazyMenuList(Long parentId, Map<String, Object> param);

	/**
	 * 树形结构
	 *
	 * @return
	 */
	List<TreeNode> tree();

	/**
	 * 授权树形结构
	 *
	 * @return
	 */
	List<TreeNode> grantTree();

	/**
	 * 授权树形结构
	 *
	 * @param roleId
	 * @return
	 */
	List<TreeNode> grantTreeByRole(List<Long> roleId);

	/**
	 * 顶部菜单树形结构
	 *
	 * @return
	 */
	List<TreeNode> grantTopTree();

	/**
	 * 顶部菜单树形结构
	 *
	 * @param roleId
	 * @return
	 */
	List<TreeNode> grantTopTreeByRole(List<Long> roleId);

	/**
	 * 数据权限授权树形结构
	 *
	 * @return
	 */
	List<TreeNode> grantDataScopeTree();

	/**
	 * 接口权限授权树形结构
	 *
	 * @return
	 */
	List<TreeNode> grantApiScopeTree();

	/**
	 * 数据权限授权树形结构
	 *
	 * @param roleId
	 * @return
	 */
	List<TreeNode> grantDataScopeTreeByRole(List<Long> roleId);

	/**
	 * 接口权限授权树形结构
	 *
	 * @param roleId
	 * @return
	 */
	List<TreeNode> grantApiScopeTreeByRole(List<Long> roleId);

	/**
	 * 所有菜单
	 *
	 * @return
	 */
	List<Menu> allMenu();

	/**
	 * 权限配置菜单
	 *
	 * @param roleId
	 * @param topMenuId
	 * @return
	 */
	List<Menu> roleMenu(List<Long> roleId, Long topMenuId);

	/**
	 * 权限配置菜单
	 *
	 * @param roleId
	 * @return
	 */
	List<Menu> roleMenuByRoleId(List<Long> roleId);

	/**
	 * 权限配置菜单
	 *
	 * @param topMenuId
	 * @return
	 */
	List<Menu> roleMenuByTopMenuId(Long topMenuId);

	/**
	 * 所有菜单
	 *
	 * @return
	 */
	List<Menu> allMenuExt();

	/**
	 * 权限配置菜单
	 *
	 * @param roleId
	 * @param topMenuId
	 * @return
	 */
	List<Menu> roleMenuExt(List<Long> roleId, Long topMenuId);

	/**
	 * 菜单树形结构
	 *
	 * @param roleId
	 * @return
	 */
	List<Menu> routes(List<Long> roleId);

	/**
	 * 按钮树形结构
	 *
	 * @return
	 */
	List<Menu> allButtons();

	/**
	 * 按钮树形结构
	 *
	 * @param roleId
	 * @return
	 */
	List<Menu> buttons(List<Long> roleId);

	/**
	 * 获取配置的角色权限
	 *
	 * @param roleIds
	 * @return
	 */
	List<MenuDTO> authRoutes(List<Long> roleIds);
}

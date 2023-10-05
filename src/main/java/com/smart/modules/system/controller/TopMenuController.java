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
package com.smart.modules.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import com.smart.core.boot.ctrl.SmartController;
import com.smart.core.cache.utils.CacheUtil;
import com.smart.core.launch.constant.AppConstant;
import com.smart.core.mp.support.Condition;
import com.smart.core.mp.support.Query;
import com.smart.core.secure.annotation.PreAuth;
import com.smart.core.tenant.annotation.NonDS;
import com.smart.core.tool.api.R;
import com.smart.core.tool.constant.RoleConstant;
import com.smart.core.tool.utils.Func;
import com.smart.modules.system.entity.TopMenu;
import com.smart.modules.system.service.ITopMenuService;
import com.smart.modules.system.vo.GrantVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.smart.core.cache.constant.CacheConstant.MENU_CACHE;
import static com.smart.core.cache.constant.CacheConstant.SYS_CACHE;

/**
 * 顶部菜单表 控制器
 *
 * @author SmartX
 */
@NonDS
@RestController
@AllArgsConstructor
@RequestMapping(AppConstant.APPLICATION_SYSTEM_NAME + "/topmenu")
@Api(value = "顶部菜单表", tags = "顶部菜单")
@PreAuth(RoleConstant.HAS_ROLE_ADMIN)
public class TopMenuController extends SmartController {

	private final ITopMenuService topMenuService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入topMenu")
	public R<TopMenu> detail(TopMenu topMenu) {
		TopMenu detail = topMenuService.getOne(Condition.getQueryWrapper(topMenu));
		return R.data(detail);
	}

	/**
	 * 分页 顶部菜单表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入topMenu")
	public R<IPage<TopMenu>> list(TopMenu topMenu, Query query) {
		IPage<TopMenu> pages = topMenuService.page(Condition.getPage(query), Condition.getQueryWrapper(topMenu).lambda().orderByAsc(TopMenu::getSort));
		return R.data(pages);
	}

	/**
	 * 新增 顶部菜单表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入topMenu")
	public R save(@Valid @RequestBody TopMenu topMenu) {
		return R.status(topMenuService.save(topMenu));
	}

	/**
	 * 修改 顶部菜单表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入topMenu")
	public R update(@Valid @RequestBody TopMenu topMenu) {
		return R.status(topMenuService.updateById(topMenu));
	}

	/**
	 * 新增或修改 顶部菜单表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入topMenu")
	public R submit(@Valid @RequestBody TopMenu topMenu) {
		return R.status(topMenuService.saveOrUpdate(topMenu));
	}


	/**
	 * 删除 顶部菜单表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(topMenuService.deleteLogic(Func.toLongList(ids)));
	}

	/**
	 * 设置顶部菜单
	 */
	@PostMapping("/grant")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "顶部菜单配置", notes = "传入topMenuId集合以及menuId集合")
	public R grant(@RequestBody GrantVO grantVO) {
		CacheUtil.clear(SYS_CACHE);
		CacheUtil.clear(MENU_CACHE);
		CacheUtil.clear(MENU_CACHE, Boolean.FALSE);
		boolean temp = topMenuService.grant(grantVO.getTopMenuIds(), grantVO.getMenuIds());
		return R.status(temp);
	}

}

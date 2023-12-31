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
import com.smart.core.tool.api.R;
import com.smart.core.tool.constant.RoleConstant;
import com.smart.core.tool.utils.Func;
import com.smart.modules.system.entity.TenantPackage;
import com.smart.modules.system.service.ITenantPackageService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.smart.core.cache.constant.CacheConstant.SYS_CACHE;

/**
 * 租户产品表 控制器
 *
 * @author SmartX
 */
@RestController
@AllArgsConstructor
@RequestMapping(AppConstant.APPLICATION_SYSTEM_NAME + "/tenant-package")
@Api(value = "租户产品表", tags = "租户产品表接口")
public class TenantPackageController extends SmartController {

	private final ITenantPackageService tenantPackageService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入tenantPackage")
	public R<TenantPackage> detail(TenantPackage tenantPackage) {
		TenantPackage detail = tenantPackageService.getOne(Condition.getQueryWrapper(tenantPackage));
		return R.data(detail);
	}

	/**
	 * 分页 租户产品表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入tenantPackage")
	public R<IPage<TenantPackage>> list(TenantPackage tenantPackage, Query query) {
		IPage<TenantPackage> pages = tenantPackageService.page(Condition.getPage(query), Condition.getQueryWrapper(tenantPackage));
		return R.data(pages);
	}

	/**
	 * 新增 租户产品表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "新增", notes = "传入tenantPackage")
	public R save(@Valid @RequestBody TenantPackage tenantPackage) {
		return R.status(tenantPackageService.save(tenantPackage));
	}

	/**
	 * 修改 租户产品表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "修改", notes = "传入tenantPackage")
	public R update(@Valid @RequestBody TenantPackage tenantPackage) {
		return R.status(tenantPackageService.updateById(tenantPackage));
	}

	/**
	 * 新增或修改 租户产品表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "新增或修改", notes = "传入tenantPackage")
	public R submit(@Valid @RequestBody TenantPackage tenantPackage) {
		CacheUtil.clear(SYS_CACHE, Boolean.FALSE);
		return R.status(tenantPackageService.saveOrUpdate(tenantPackage));
	}


	/**
	 * 删除 租户产品表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		CacheUtil.clear(SYS_CACHE, Boolean.FALSE);
		return R.status(tenantPackageService.deleteLogic(Func.toLongList(ids)));
	}


	/**
	 * 下拉数据源
	 */
	@GetMapping("/select")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "下拉数据源", notes = "传入tenant")
	@PreAuth(RoleConstant.HAS_ROLE_ADMIN)
	public R<List<TenantPackage>> select(TenantPackage tenantPackage) {
		return R.data(tenantPackageService.list(Condition.getQueryWrapper(tenantPackage)));
	}


}

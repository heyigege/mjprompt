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
import com.smart.modules.system.wrapper.DataScopeWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import com.smart.core.boot.ctrl.SmartController;
import com.smart.core.cache.utils.CacheUtil;
import com.smart.core.launch.constant.AppConstant;
import com.smart.core.mp.support.Condition;
import com.smart.core.mp.support.Query;
import com.smart.core.tenant.annotation.NonDS;
import com.smart.core.tool.api.R;
import com.smart.core.tool.utils.Func;
import com.smart.modules.system.entity.DataScope;
import com.smart.modules.system.service.IDataScopeService;
import com.smart.modules.system.vo.DataScopeVO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.smart.core.cache.constant.CacheConstant.SYS_CACHE;

/**
 * 数据权限控制器
 *
 * @author SmartX
 */
@NonDS
@RestController
@AllArgsConstructor
@RequestMapping(AppConstant.APPLICATION_SYSTEM_NAME + "/data-scope")
@Api(value = "数据权限", tags = "数据权限")
public class DataScopeController extends SmartController {

	private final IDataScopeService dataScopeService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入dataScope")
	public R<DataScope> detail(DataScope dataScope) {
		DataScope detail = dataScopeService.getOne(Condition.getQueryWrapper(dataScope));
		return R.data(detail);
	}

	/**
	 * 分页
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入dataScope")
	public R<IPage<DataScopeVO>> list(DataScope dataScope, Query query) {
		IPage<DataScope> pages = dataScopeService.page(Condition.getPage(query), Condition.getQueryWrapper(dataScope));
		return R.data(DataScopeWrapper.build().pageVO(pages));
	}

	/**
	 * 新增
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "新增", notes = "传入dataScope")
	public R save(@Valid @RequestBody DataScope dataScope) {
		CacheUtil.clear(SYS_CACHE, Boolean.FALSE);
		return R.status(dataScopeService.save(dataScope));
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "修改", notes = "传入dataScope")
	public R update(@Valid @RequestBody DataScope dataScope) {
		CacheUtil.clear(SYS_CACHE, Boolean.FALSE);
		return R.status(dataScopeService.updateById(dataScope));
	}

	/**
	 * 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "新增或修改", notes = "传入dataScope")
	public R submit(@Valid @RequestBody DataScope dataScope) {
		CacheUtil.clear(SYS_CACHE, Boolean.FALSE);
		return R.status(dataScopeService.saveOrUpdate(dataScope));
	}


	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		CacheUtil.clear(SYS_CACHE, Boolean.FALSE);
		return R.status(dataScopeService.deleteLogic(Func.toLongList(ids)));
	}

}

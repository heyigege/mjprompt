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
package com.smart.modules.resource.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.smart.modules.resource.entity.Attach;
import com.smart.modules.resource.vo.AttachVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import com.smart.core.boot.ctrl.SmartController;
import com.smart.core.launch.constant.AppConstant;
import com.smart.core.mp.support.Condition;
import com.smart.core.mp.support.Query;
import com.smart.core.tenant.annotation.NonDS;
import com.smart.core.tool.api.R;
import com.smart.core.tool.utils.Func;
import com.smart.modules.resource.service.IAttachService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 附件表 控制器
 *
 * @author Chill
 */
@NonDS
@RestController
@AllArgsConstructor
@RequestMapping(AppConstant.APPLICATION_RESOURCE_NAME + "/attach")
@Api(value = "附件", tags = "附件")
public class AttachController extends SmartController {

	private final IAttachService attachService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入attach")
	public R<Attach> detail(Attach attach) {
		Attach detail = attachService.getOne(Condition.getQueryWrapper(attach));
		return R.data(detail);
	}

	/**
	 * 分页 附件表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入attach")
	public R<IPage<Attach>> list(Attach attach, Query query) {
		IPage<Attach> pages = attachService.page(Condition.getPage(query), Condition.getQueryWrapper(attach));
		return R.data(pages);
	}

	/**
	 * 自定义分页 附件表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入attach")
	public R<IPage<AttachVO>> page(AttachVO attach, Query query) {
		IPage<AttachVO> pages = attachService.selectAttachPage(Condition.getPage(query), attach);
		return R.data(pages);
	}

	/**
	 * 新增 附件表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入attach")
	public R save(@Valid @RequestBody Attach attach) {
		return R.status(attachService.save(attach));
	}

	/**
	 * 修改 附件表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入attach")
	public R update(@Valid @RequestBody Attach attach) {
		return R.status(attachService.updateById(attach));
	}

	/**
	 * 新增或修改 附件表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入attach")
	public R submit(@Valid @RequestBody Attach attach) {
		return R.status(attachService.saveOrUpdate(attach));
	}


	/**
	 * 删除 附件表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(attachService.deleteLogic(Func.toLongList(ids)));
	}


}

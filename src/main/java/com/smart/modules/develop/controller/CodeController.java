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
package com.smart.modules.develop.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.smart.modules.develop.entity.Code;
import com.smart.modules.develop.entity.Datasource;
import com.smart.modules.develop.service.ICodeService;
import com.smart.modules.develop.service.IDatasourceService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import com.smart.core.boot.ctrl.SmartController;
import com.smart.core.launch.constant.AppConstant;
import com.smart.core.mp.support.Condition;
import com.smart.core.mp.support.Query;
import com.smart.core.secure.annotation.PreAuth;
import com.smart.core.tenant.annotation.NonDS;
import com.smart.core.tool.api.R;
import com.smart.core.tool.constant.RoleConstant;
import com.smart.core.tool.utils.Func;
import com.smart.develop.support.SmartCodeGenerator;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

/**
 * 控制器
 *
 * @author Chill
 */
@NonDS
@ApiIgnore
@RestController
@AllArgsConstructor
@RequestMapping(AppConstant.APPLICATION_DEVELOP_NAME + "/code")
@Api(value = "代码生成", tags = "代码生成")
@PreAuth(RoleConstant.HAS_ROLE_ADMINISTRATOR)
public class CodeController extends SmartController {

	private final ICodeService codeService;
	private final IDatasourceService datasourceService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入code")
	public R<Code> detail(Code code) {
		Code detail = codeService.getOne(Condition.getQueryWrapper(code));
		return R.data(detail);
	}

	/**
	 * 分页
	 */
	@GetMapping("/list")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "codeName", value = "模块名", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "tableName", value = "表名", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "modelName", value = "实体名", paramType = "query", dataType = "string")
	})
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入code")
	public R<IPage<Code>> list(@ApiIgnore @RequestParam Map<String, Object> code, Query query) {
		IPage<Code> pages = codeService.page(Condition.getPage(query), Condition.getQueryWrapper(code, Code.class));
		return R.data(pages);
	}

	/**
	 * 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "新增或修改", notes = "传入code")
	public R submit(@Valid @RequestBody Code code) {
		return R.status(codeService.submit(code));
	}


	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(codeService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 复制
	 */
	@PostMapping("/copy")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "复制", notes = "传入id")
	public R copy(@ApiParam(value = "主键", required = true) @RequestParam Long id) {
		Code code = codeService.getById(id);
		code.setId(null);
		code.setCodeName(code.getCodeName() + "-copy");
		return R.status(codeService.save(code));
	}

	/**
	 * 代码生成
	 */
	@PostMapping("/gen-code")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "代码生成", notes = "传入ids")
	public R genCode(@ApiParam(value = "主键集合", required = true) @RequestParam String ids, @RequestParam(defaultValue = "sword") String system) {
		Collection<Code> codes = codeService.listByIds(Func.toLongList(ids));
		codes.forEach(code -> {
			SmartCodeGenerator generator = new SmartCodeGenerator();
			// 设置数据源
			Datasource datasource = datasourceService.getById(code.getDatasourceId());
			generator.setDriverName(datasource.getDriverClass());
			generator.setUrl(datasource.getUrl());
			generator.setUsername(datasource.getUsername());
			generator.setPassword(datasource.getPassword());
			// 设置基础配置
			generator.setSystemName(system);
			generator.setCodeName(code.getCodeName());
			generator.setServiceName(code.getServiceName());
			generator.setPackageName(code.getPackageName());
			generator.setPackageDir(code.getApiPath());
			generator.setPackageWebDir(code.getWebPath());
			generator.setTablePrefix(Func.toStrArray(code.getTablePrefix()));
			generator.setIncludeTables(Func.toStrArray(code.getTableName()));
			// 设置是否继承基础业务字段
			generator.setHasSuperEntity(code.getBaseMode() == 2);
			// 设置是否开启包装器模式
			generator.setHasWrapper(code.getWrapMode() == 2);
			generator.run();
		});
		return R.success("代码生成成功");
	}

}

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
import com.smart.modules.system.wrapper.DictBizWrapper;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import com.smart.core.boot.ctrl.SmartController;
import com.smart.core.cache.utils.CacheUtil;
import com.smart.core.launch.constant.AppConstant;
import com.smart.core.mp.support.Condition;
import com.smart.core.mp.support.Query;
import com.smart.core.tenant.annotation.NonDS;
import com.smart.core.tool.api.R;
import com.smart.modules.system.entity.DictBiz;
import com.smart.modules.system.service.IDictBizService;
import com.smart.modules.system.vo.DictBizVO;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.smart.core.cache.constant.CacheConstant.DICT_CACHE;

/**
 * 控制器
 *
 * @author Chill
 */
@NonDS
@RestController
@AllArgsConstructor
@RequestMapping(AppConstant.APPLICATION_SYSTEM_NAME + "/dict-biz")
@Api(value = "业务字典", tags = "业务字典")
public class DictBizController extends SmartController {

	private final IDictBizService dictService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入dict")
	public R<DictBizVO> detail(DictBiz dict) {
		DictBiz detail = dictService.getOne(Condition.getQueryWrapper(dict));
		return R.data(DictBizWrapper.build().entityVO(detail));
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "code", value = "字典编号", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "dictValue", value = "字典名称", paramType = "query", dataType = "string")
	})
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "列表", notes = "传入dict")
	public R<List<DictBizVO>> list(@ApiIgnore @RequestParam Map<String, Object> dict) {
		List<DictBiz> list = dictService.list(Condition.getQueryWrapper(dict, DictBiz.class).lambda().orderByAsc(DictBiz::getSort));
		return R.data(DictBizWrapper.build().listNodeVO(list));
	}

	/**
	 * 顶级列表
	 */
	@GetMapping("/parent-list")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "code", value = "字典编号", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "dictValue", value = "字典名称", paramType = "query", dataType = "string")
	})
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "列表", notes = "传入dict")
	public R<IPage<DictBizVO>> parentList(@ApiIgnore @RequestParam Map<String, Object> dict, Query query) {
		return R.data(dictService.parentList(dict, query));
	}

	/**
	 * 子列表
	 */
	@GetMapping("/child-list")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "code", value = "字典编号", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "dictValue", value = "字典名称", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "parentId", value = "字典名称", paramType = "query", dataType = "string")
	})
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "列表", notes = "传入dict")
	public R<List<DictBizVO>> childList(@ApiIgnore @RequestParam Map<String, Object> dict, @RequestParam(required = false, defaultValue = "-1") Long parentId) {
		return R.data(dictService.childList(dict, parentId));
	}

	/**
	 * 获取字典树形结构
	 */
	@GetMapping("/tree")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "树形结构", notes = "树形结构")
	public R<List<DictBizVO>> tree() {
		List<DictBizVO> tree = dictService.tree();
		return R.data(tree);
	}

	/**
	 * 获取字典树形结构
	 */
	@GetMapping("/parent-tree")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "树形结构", notes = "树形结构")
	public R<List<DictBizVO>> parentTree() {
		List<DictBizVO> tree = dictService.parentTree();
		return R.data(tree);
	}

	/**
	 * 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入dict")
	public R submit(@Valid @RequestBody DictBiz dict) {
		CacheUtil.clear(DICT_CACHE);
		return R.status(dictService.submit(dict));
	}


	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		CacheUtil.clear(DICT_CACHE);
		return R.status(dictService.removeDict(ids));
	}

	/**
	 * 获取字典
	 */
	@GetMapping("/dictionary")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "获取字典", notes = "获取字典")
	public R<List<DictBiz>> dictionary(String code) {
		List<DictBiz> tree = dictService.getList(code);
		return R.data(tree);
	}

	/**
	 * 获取字典树
	 */
	@GetMapping("/dictionary-tree")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "获取字典树", notes = "获取字典树")
	public R<List<DictBizVO>> dictionaryTree(String code) {
		List<DictBiz> tree = dictService.getList(code);
		return R.data(DictBizWrapper.build().listNodeVO(tree));
	}




	/**
	 * 根据parentId获取子级的列表
	 */
	@GetMapping("/child-list-parentId")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "parentId", value = "父级id", paramType = "query", dataType = "string")
	})
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "列表", notes = "传入dict")
	public R<List<DictBiz>> childListByParentId(@RequestParam(required = true) Long parentId) {
		List<DictBiz> list = dictService.lambdaQuery().eq(DictBiz::getParentId, parentId).orderByAsc(DictBiz::getSort).select(DictBiz::getDictKey, DictBiz::getDictValue, DictBiz::getId).list();
		return R.data(list);
	}


	/**
	 * 传入parentId 获取树形结构
	 */
	@GetMapping("/getChildTree")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "传入parentId 获取树形结构", notes = "树形结构")
	public R<List<DictBizVO>> getChildTree(@RequestParam(required = true) Long parentId) {
		List<DictBizVO> tree = dictService.getChildTree(parentId);
		return R.data(tree);
	}

}

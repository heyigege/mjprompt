package com.smart.modules.mjPrompt.controller;

import io.swagger.annotations.Api;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

import javax.validation.Valid;

import com.smart.core.boot.ctrl.SmartController;
import com.smart.common.constant.CommonConstant;

import com.smart.core.mp.support.Condition;
import com.smart.core.mp.support.Query;
import com.smart.core.tool.api.R;
import com.smart.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.smart.modules.mjPrompt.entity.MjPromptEntity;
import com.smart.modules.mjPrompt.vo.MjPromptVO;
import com.smart.modules.mjPrompt.wrapper.MjPromptWrapper;
import com.smart.modules.mjPrompt.service.IMjPromptService;


/**
 * 控制器
 *
 * @author SmartX
 */
@RestController
@AllArgsConstructor
@RequestMapping("/smart-mj/mj_prompt")
@Api(value = "", tags = "")
public class MjPromptController extends SmartController {

	private final IMjPromptService mjPromptService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入mj_prompt")
	public R<MjPromptEntity> detail(MjPromptEntity mj_prompt) {
		MjPromptEntity detail = mjPromptService.getOne(Condition.getQueryWrapper(mj_prompt));
		return R.data(detail);
	}

	/**
	 * 分页 代码自定义代号
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入mj_prompt")
	public R<IPage<MjPromptEntity>> list(MjPromptEntity mj_prompt, Query query) {
		IPage<MjPromptEntity> pages = mjPromptService.page(Condition.getPage(query), Condition.getQueryWrapper(mj_prompt));
		return R.data(pages);
	}

	/**
	 * 新增 代码自定义代号
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入mj_prompt")
	public R save(@Valid @RequestBody MjPromptEntity mj_prompt) {
		return R.status(mjPromptService.save(mj_prompt));
	}

	/**
	 * 修改 代码自定义代号
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入mj_prompt")
	public R update(@Valid @RequestBody MjPromptEntity mj_prompt) {
		return R.status(mjPromptService.updateById(mj_prompt));
	}

	/**
	 * 新增或修改 代码自定义代号
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入mj_prompt")
	public R submit(@Valid @RequestBody MjPromptEntity mj_prompt) {
		return R.status(mjPromptService.saveOrUpdate(mj_prompt));
	}


	/**
	 * 删除 代码自定义代号
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(mjPromptService.deleteLogic(Func.toLongList(ids)));
	}

}

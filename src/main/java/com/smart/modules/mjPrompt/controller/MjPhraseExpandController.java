package com.smart.modules.mjPrompt.controller;

import com.smart.core.cache.utils.CacheUtil;
import com.smart.core.tool.utils.ThreadUtil;
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

import com.smart.modules.mjPrompt.entity.MjPhraseExpandEntity;
import com.smart.modules.mjPrompt.vo.MjPhraseExpandVO;
import com.smart.modules.mjPrompt.wrapper.MjPhraseExpandWrapper;
import com.smart.modules.mjPrompt.service.IMjPhraseExpandService;

import static com.smart.common.constant.MjAppConstant.Mj_APPID_CACHE;
import static com.smart.common.constant.MjAppConstant.Mj_PHRASE_CACHE;


/**
 * 控制器
 *
 * @author SmartX
 */
@RestController
@AllArgsConstructor
@RequestMapping("/smart-mj/mj_phrase_expand")
@Api(value = "", tags = "")
public class MjPhraseExpandController extends SmartController {

	private final IMjPhraseExpandService mjPhraseExpandService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入mj_phrase_expand")
	public R<MjPhraseExpandEntity> detail(MjPhraseExpandEntity mj_phrase_expand) {
		MjPhraseExpandEntity detail = mjPhraseExpandService.getOne(Condition.getQueryWrapper(mj_phrase_expand));
		return R.data(detail);
	}

	/**
	 * 分页 代码自定义代号
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入mj_phrase_expand")
	public R<IPage<MjPhraseExpandEntity>> list(MjPhraseExpandEntity mj_phrase_expand, Query query) {
		IPage<MjPhraseExpandEntity> pages = mjPhraseExpandService.page(Condition.getPage(query), Condition.getQueryWrapper(mj_phrase_expand));
		return R.data(pages);
	}

	/**
	 * 新增 代码自定义代号
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入mj_phrase_expand")
	public R save(@Valid @RequestBody MjPhraseExpandEntity mj_phrase_expand) {
		CacheUtil.clear(Mj_PHRASE_CACHE);
		return R.status(mjPhraseExpandService.save(mj_phrase_expand));
	}

	/**
	 * 修改 代码自定义代号
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入mj_phrase_expand")
	public R update(@Valid @RequestBody MjPhraseExpandEntity mj_phrase_expand) {
		CacheUtil.clear(Mj_PHRASE_CACHE);
		CacheUtil.clear(Mj_PHRASE_CACHE, Boolean.FALSE);

		return R.status(mjPhraseExpandService.updateById(mj_phrase_expand));
	}

	/**
	 * 新增或修改 代码自定义代号
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入mj_phrase_expand")
	public R submit(@Valid @RequestBody MjPhraseExpandEntity mj_phrase_expand) {
		return R.status(mjPhraseExpandService.saveOrUpdate(mj_phrase_expand));
	}


	/**
	 * 删除 代码自定义代号
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		CacheUtil.clear(Mj_PHRASE_CACHE);
		return R.status(mjPhraseExpandService.deleteLogic(Func.toLongList(ids)));
	}

}

package com.smart.modules.mjPrompt.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.smart.core.boot.ctrl.SmartController;
import com.smart.core.mp.support.Condition;
import com.smart.core.mp.support.Query;
import com.smart.core.tool.api.R;
import com.smart.core.tool.utils.Func;
import com.smart.modules.mjPrompt.entity.MjPhraseEntity;
import com.smart.modules.mjPrompt.service.IMjPhraseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * 控制器
 *
 * @author SmartX
 */
@RestController
@AllArgsConstructor
@RequestMapping("/smart-mj/mj_phrase")
@Api(value = "", tags = "")
public class MjPhraseController extends SmartController {

	private final IMjPhraseService mj_phraseService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入mj_phrase")
	public R<MjPhraseEntity> detail(MjPhraseEntity mj_phrase) {
		MjPhraseEntity detail = mj_phraseService.getOne(Condition.getQueryWrapper(mj_phrase));
		return R.data(detail);
	}

	/**
	 * 分页 代码自定义代号
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入mj_phrase")
	public R<IPage<MjPhraseEntity>> list(MjPhraseEntity mj_phrase, Query query) {
		IPage<MjPhraseEntity> pages = mj_phraseService.page(Condition.getPage(query), Condition.getQueryWrapper(mj_phrase));
		return R.data(pages);
	}

	/**
	 * 新增 代码自定义代号
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入mj_phrase")
	public R save(@Valid @RequestBody MjPhraseEntity mj_phrase) {
		return R.status(mj_phraseService.save(mj_phrase));
	}

	/**
	 * 修改 代码自定义代号
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入mj_phrase")
	public R update(@Valid @RequestBody MjPhraseEntity mj_phrase) {
		return R.status(mj_phraseService.updateById(mj_phrase));
	}

	/**
	 * 新增或修改 代码自定义代号
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入mj_phrase")
	public R submit(@Valid @RequestBody MjPhraseEntity mj_phrase) {
		return R.status(mj_phraseService.saveOrUpdate(mj_phrase));
	}


	/**
	 * 删除 代码自定义代号
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(mj_phraseService.deleteLogic(Func.toLongList(ids)));
	}

	@PostMapping("/enable")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "启用停用")
	public R<Boolean> setEnable(@RequestBody MjPhraseEntity mjPhrase) {

		int status = 1;
		if (mjPhrase.getStatus() == 1) {
			status = 2;
		}

		boolean update = mj_phraseService.lambdaUpdate().eq(MjPhraseEntity::getId, mjPhrase.getId()).set(MjPhraseEntity::getStatus, status).update();
		return R.status(update);
	}

}

package com.smart.modules.mjPrompt.controller;

import com.smart.common.vo.KeyValueVo;
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

import com.smart.modules.mjPrompt.entity.MjAppEntity;
import com.smart.modules.mjPrompt.vo.MjAppVO;
import com.smart.modules.mjPrompt.wrapper.MjAppWrapper;
import com.smart.modules.mjPrompt.service.IMjAppService;

import java.util.List;
import java.util.stream.Collectors;

import static com.smart.common.utils.CommonUtil.getRandomString;


/**
 *  控制器
 *
 * @author SmartX
 */
@RestController
@AllArgsConstructor
@RequestMapping("/smart-mj/mj_app")
@Api(value = "", tags = "")
public class MjAppController extends SmartController {

	private final IMjAppService mj_appService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入mj_app")
	public R<MjAppEntity> detail(MjAppEntity mj_app) {
		MjAppEntity detail = mj_appService.getOne(Condition.getQueryWrapper(mj_app));
		return R.data(detail);
	}

	/**
	 * 分页 代码自定义代号
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入mj_app")
	public R<IPage<MjAppEntity>> list(MjAppEntity mj_app, Query query) {
		IPage<MjAppEntity> pages = mj_appService.page(Condition.getPage(query), Condition.getQueryWrapper(mj_app));
		return R.data(pages);
	}

	/**
	 * 新增 代码自定义代号
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入mj_app")
	public R save(@Valid @RequestBody MjAppEntity mj_app) {
		mj_app.setAppCode(getRandomString(6));
		return R.status(mj_appService.save(mj_app));
	}

	/**
	 * 修改 代码自定义代号
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入mj_app")
	public R update(@Valid @RequestBody MjAppEntity mj_app) {
		return R.status(mj_appService.updateById(mj_app));
	}

	/**
	 * 新增或修改 代码自定义代号
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入mj_app")
	public R submit(@Valid @RequestBody MjAppEntity mj_app) {
		return R.status(mj_appService.saveOrUpdate(mj_app));
	}


	/**
	 * 删除 代码自定义代号
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(mj_appService.deleteLogic(Func.toLongList(ids)));
	}
	@GetMapping("/getSelect")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "下拉选项", notes = "获取下拉选项")
	public R<List<KeyValueVo>> getSelect() {
		List<KeyValueVo> list = mj_appService.list(Condition.getQueryWrapper(new MjAppEntity())).stream().map(mj_app -> KeyValueVo.builder().value(mj_app.getId()).key(mj_app.getAppName()).build()).collect(Collectors.toList());
		return R.data(list);
	}
}

package com.smart.modules.mjPrompt.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.smart.core.boot.ctrl.SmartController;
import com.smart.core.cache.utils.CacheUtil;
import com.smart.core.mp.support.Condition;
import com.smart.core.mp.support.Query;
import com.smart.core.tool.api.R;
import com.smart.core.tool.utils.BeanUtil;
import com.smart.core.tool.utils.Func;
import com.smart.modules.mjPrompt.dto.MjPhraseExpandDTO;
import com.smart.modules.mjPrompt.entity.MjPhraseExpandEntity;
import com.smart.modules.mjPrompt.service.IMjPhraseExpandService;
import com.smart.modules.system.mapper.DictBizMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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
	@Resource
	private DictBizMapper dictBizMapper;

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
	public R<IPage<MjPhraseExpandDTO>> list(MjPhraseExpandEntity mj_phrase_expand, Query query) {
		IPage<MjPhraseExpandEntity> pages = mjPhraseExpandService.page(Condition.getPage(query), Condition.getQueryWrapper(mj_phrase_expand));
		IPage<MjPhraseExpandDTO> iPage = new Page();
		List<MjPhraseExpandDTO> mjPhraseExpandList = new ArrayList<>();

		pages.getRecords().stream().forEach(e -> {
			MjPhraseExpandDTO mjPhraseExpandDTO = new MjPhraseExpandDTO();

			BeanUtil.copyProperties(e, mjPhraseExpandDTO);
			mjPhraseExpandDTO.setDictbizString(dictBizMapper.getDictValuesString(e.getDictbizId()));
			mjPhraseExpandList.add(mjPhraseExpandDTO);
		});
		iPage.setRecords(mjPhraseExpandList);
		iPage.setPages(pages.getPages());
		iPage.setCurrent(pages.getCurrent());
		iPage.setSize(pages.getSize());
		iPage.setTotal(pages.getTotal());

		return R.data(iPage);
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

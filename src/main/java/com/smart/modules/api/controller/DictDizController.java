package com.smart.modules.api.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.smart.core.boot.ctrl.SmartController;
import com.smart.core.tool.api.R;
import com.smart.modules.api.dto.DictDizeGenerateRequestDTO;
import com.smart.modules.api.service.DictDizService;
import com.smart.modules.system.entity.DictBiz;
import com.smart.modules.system.service.IDictBizService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dictDiz")
@Slf4j
public class DictDizController extends SmartController {
	@Autowired

	private IDictBizService dictService;

	@Autowired
	private DictDizService dictDizService;

	@PostMapping("/generate")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "生成词典")

	public R generate(@RequestBody DictDizeGenerateRequestDTO dictDizeGenerateRequestDTO) {

		Boolean res = dictDizService.generate(dictDizeGenerateRequestDTO);
		return R.status(res);
	}


	@GetMapping("/dictionary")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "获取字典", notes = "获取字典")
	public R<List<DictBiz>> dictionary(String code) {
		List<DictBiz> tree = dictService.getList(code);
		return R.data(tree);
	}


}

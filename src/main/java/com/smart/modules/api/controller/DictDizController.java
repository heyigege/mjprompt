package com.smart.modules.api.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.smart.core.boot.ctrl.SmartController;
import com.smart.core.tool.api.R;
import com.smart.modules.api.dto.DictDizeGenerateRequestDTO;
import com.smart.modules.api.dto.mjPromptRequestDTO;
import com.smart.modules.api.model.dto.link_aI.CompletionBody;
import com.smart.modules.api.service.DictDizService;
import com.smart.modules.api.service.LinkAIService;
import com.smart.modules.system.entity.DictBiz;
import com.smart.modules.system.mapper.DictBizMapper;
import com.smart.modules.system.service.IDictBizService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dictDiz")
@Slf4j
public class DictDizController extends SmartController {

	@Autowired
	private DictDizService dictDizService;

	@PostMapping("/generate")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "生成词典")

	public R generate(@RequestBody DictDizeGenerateRequestDTO dictDizeGenerateRequestDTO) {

		Boolean res = dictDizService.generate(dictDizeGenerateRequestDTO);
		return R.status(res);
	}

}

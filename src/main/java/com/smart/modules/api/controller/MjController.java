package com.smart.modules.api.controller;

import com.alibaba.fastjson.JSON;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.smart.core.boot.ctrl.SmartController;
import com.smart.core.cache.utils.CacheUtil;
import com.smart.modules.api.model.dto.link_aI.CompletionBody;
import com.smart.modules.api.service.LinkAIService;
import com.smart.modules.mjPrompt.entity.MjAppEntity;
import com.smart.modules.mjPrompt.entity.MjPhraseEntity;
import com.smart.modules.mjPrompt.mapper.MjPhraseMapper;
import com.smart.modules.mjPrompt.service.IMjAppService;
import com.smart.modules.mjPrompt.service.IMjPhraseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.smart.core.tool.api.R;

import java.util.*;

import static com.smart.common.constant.MjAppConstant.Mj_APPID_CACHE;
import static com.smart.common.constant.MjAppConstant.Mj_PHRASE_CACHE;

@RestController
@AllArgsConstructor
@RequestMapping("/api/mj")
@Api(value = "", tags = "")
@Slf4j
public class MjController extends SmartController {

	@Autowired
	private IMjAppService mjAppService;

	@Autowired
	private IMjPhraseService mjPhraseService;
	private MjPhraseMapper mjPhraseMapper;

	@Autowired
	private LinkAIService linkAIService;

	@GetMapping("/getMjPrompt")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "获取MjPrompt", notes = "传入appCode")
	public R getMjPrompt(String appCode) {

		MjAppEntity mjApp = mjAppService.lambdaQuery().eq(MjAppEntity::getAppCode, appCode).eq(MjAppEntity::getStatus, 1).one();

		if (mjApp == null) {
			return R.fail("appCode不存在");
		}

		StringBuilder stringBuilder = new StringBuilder();

		String mjPhrase = CacheUtil.get(Mj_PHRASE_CACHE, Mj_APPID_CACHE, appCode, () -> mjPhraseService.handlePhraseCache(appCode));

		ArrayList<Map> mjList = JSON.parseObject(mjPhrase, ArrayList.class);

		for (Map map : mjList) {
			List keyList = (List) map.get("keyList");
			Integer maxNum = (Integer) map.get("maxNum");
			Integer minNum = (Integer) map.get("minNum");
			if (maxNum > keyList.size()) {
				maxNum = keyList.size();
			}
			if (keyList.size() < minNum) {
				minNum = keyList.size();
			}
			int randomNum = new Random().nextInt(maxNum) % (maxNum - minNum + 1) + minNum;
			for (int i = 0; i < randomNum; i++) {
				stringBuilder.append(keyList.get(new Random().nextInt(keyList.size())));
				stringBuilder.append(",");
			}
		}
		log.info(stringBuilder.toString());

		CompletionBody completionBody = new CompletionBody();
		completionBody.setPrompt(stringBuilder.toString());
		completionBody.setToken(mjApp.getLinkAiToken());
		completionBody.setAppCode(mjApp.getLinkAiCode());
		String completions = linkAIService.getCompletions(completionBody);
		log.info(completions);
		Map<String, String> promptMap = JSON.parseObject(completions, Map.class);
		String phraseSuffix = mjPhraseMapper.getPhraseSuffixByAppCode(appCode);
		return R.success(promptMap.get("prompt") + phraseSuffix);
	}
}

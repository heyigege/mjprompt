package com.smart.modules.api.controller;

import com.alibaba.fastjson.JSON;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.gson.Gson;
import com.smart.core.boot.ctrl.SmartController;
import com.smart.core.cache.utils.CacheUtil;
import com.smart.core.tool.api.R;
import com.smart.modules.api.dto.MjPromptRequestDTO;
import com.smart.modules.api.dto.MjPromptResponseDTO;
import com.smart.modules.api.model.dto.link_aI.CompletionBody;
import com.smart.modules.api.service.LinkAIService;
import com.smart.modules.mjPrompt.dto.MjAppConfigDTO;
import com.smart.modules.mjPrompt.entity.MjAppEntity;
import com.smart.modules.mjPrompt.entity.MjPhraseEntity;
import com.smart.modules.mjPrompt.entity.MjPromptEntity;
import com.smart.modules.mjPrompt.mapper.MjAppConfigMapper;
import com.smart.modules.mjPrompt.mapper.MjPhraseMapper;
import com.smart.modules.mjPrompt.service.IMjAppService;
import com.smart.modules.mjPrompt.service.IMjPhraseService;
import com.smart.modules.mjPrompt.service.IMjPromptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.smart.common.constant.MjAppConstant.Mj_APPID_CACHE;
import static com.smart.common.constant.MjAppConstant.Mj_PHRASE_CACHE;

@RestController
@AllArgsConstructor
@RequestMapping("/api/mj")
@Api(value = "", tags = "")
@Slf4j
public class MjController extends SmartController {

	@Autowired

	private final IMjPromptService mjPromptService;

	@Resource
	private MjAppConfigMapper mjAppConfigMapper;
	@Autowired
	private IMjAppService mjAppService;
	@Autowired
	private IMjPhraseService mjPhraseService;
	private MjPhraseMapper mjPhraseMapper;
	@Autowired
	private LinkAIService linkAIService;

	@PostMapping("/getMjPrompt")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "获取MjPrompt", notes = "传入appCode")
	public R getMjPrompt(@RequestBody MjPromptRequestDTO mjPromptRequestDTO) {
		String promptString = "";
		MjAppEntity mjApp = mjAppService.lambdaQuery().eq(MjAppEntity::getAppCode, mjPromptRequestDTO.getAppCode()).eq(MjAppEntity::getStatus, 1).one();
		if (mjApp == null) {
			return R.fail("appCode不存在");
		}

		ArrayList<String> promptList = new ArrayList<>();
		String mjPhrase = CacheUtil.get(Mj_PHRASE_CACHE, Mj_APPID_CACHE, mjPromptRequestDTO.getAppCode(), () -> mjPhraseService.handlePhraseCache(mjPromptRequestDTO.getAppCode()));
		ArrayList<Map> mjList = JSON.parseObject(mjPhrase, ArrayList.class);
		for (Map map : mjList) {
			List<String> keyList = (List) map.get("keyList");
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
				String phrase = keyList.get(new Random().nextInt(keyList.size()));
				if (!promptList.contains(phrase)) {
					promptList.add(phrase);
				}
			}
		}

		String prompt = promptList.stream().collect(Collectors.joining(","));
		log.info(prompt);

		CompletionBody completionBody = new CompletionBody();
		completionBody.setPrompt(prompt);
		completionBody.setToken(mjApp.getLinkAiToken());
		completionBody.setAppCode(mjApp.getLinkAiCode());
		String completions = linkAIService.getCompletions(completionBody);
		log.info(completions);
		Map<String, String> promptMap = JSON.parseObject(completions, Map.class);
		String phraseSuffix = mjPhraseMapper.getPhraseSuffixByAppCode(mjPromptRequestDTO.getAppCode());

		String url = "";
		MjPhraseEntity mjPhraseEntity = mjPhraseService.lambdaQuery().eq(MjPhraseEntity::getAppId, mjApp.getId()).eq(MjPhraseEntity::getStatus, 1).last("limit 1").one();
		if (mjPhraseEntity.getIsPicture() == 2) {
			if (!("".equals(mjPhraseEntity.getPictureUrl()))) {
				String[] pictureUrlSplit = mjPhraseEntity.getPictureUrl().split(",");
				int urlNum = new Random().nextInt(pictureUrlSplit.length);
				url = pictureUrlSplit[urlNum];
				promptString += url + " , ";
			}

		}
		promptString += (promptMap.get("prompt") + " " + phraseSuffix);

		MjPromptEntity mjPromptEntity = new MjPromptEntity();
		mjPromptEntity.setAppId(mjApp.getId());
		mjPromptEntity.setPrompt(promptString);
		mjPromptEntity.setPromptOrigin(prompt);
		mjPromptEntity.setPhraseSuffix(phraseSuffix);

		mjPromptEntity.setPhraseId(mjPhraseEntity.getId());
		mjPromptService.save(mjPromptEntity);
		MjPromptResponseDTO mjPromptResponseDTO = new MjPromptResponseDTO();

		mjPromptResponseDTO.setPromptOrigin(promptMap.get("prompt"));
		mjPromptResponseDTO.setPrompt(promptString);
		return R.data(mjPromptResponseDTO);
	}

	@PostMapping("/getMjAppConfig")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "获取MjAppConfig", notes = "传入appCode")
	public R getMjAppConfig(@RequestBody MjPromptRequestDTO mjPromptRequestDTO) {

		MjAppConfigDTO mjAppConfigDTO = mjAppConfigMapper.getAppConfig(mjPromptRequestDTO.getAppCode());
		if (mjAppConfigDTO == null) {
			return R.fail("appCode不存在");
		}
		if (mjAppConfigDTO.getEmailConfig() != null) {

			Gson gson = new Gson();
			HashMap<String, String> emailConfigMap = gson.fromJson(mjAppConfigDTO.getEmailConfig(), HashMap.class);
			mjAppConfigDTO.setMailSender(emailConfigMap.get("mailSender"));
			mjAppConfigDTO.setMailSenderName(emailConfigMap.get("mailSenderName"));
			mjAppConfigDTO.setMailRecipient(emailConfigMap.get("mailRecipient"));
			mjAppConfigDTO.setMailSubject(emailConfigMap.get("mailSubject"));
			mjAppConfigDTO.setMailBody(emailConfigMap.get("mailBody"));
			mjAppConfigDTO.setSmtpServer(emailConfigMap.get("smtpServer"));
			mjAppConfigDTO.setSmtpUsername(emailConfigMap.get("smtpUsername"));
			mjAppConfigDTO.setSmtpPassword(emailConfigMap.get("smtpPassword"));
		}
		return R.data(mjAppConfigDTO);
	}


}

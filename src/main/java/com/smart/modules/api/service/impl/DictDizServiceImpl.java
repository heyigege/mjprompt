package com.smart.modules.api.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import com.smart.modules.api.dto.DictDizeGenerateRequestDTO;
import com.smart.modules.api.model.dto.link_aI.CompletionBody;
import com.smart.modules.api.service.DictDizService;
import com.smart.modules.api.service.LinkAIService;
import com.smart.modules.system.entity.DictBiz;
import com.smart.modules.system.mapper.DictBizMapper;
import com.smart.modules.system.service.IDictBizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DictDizServiceImpl implements DictDizService {

	@Resource
	private IDictBizService dictBizService;

	@Resource
	private DictBizMapper dictBizMapper;

	@Resource
	private LinkAIService linkAIService;


	@Override
	public Boolean generate(DictDizeGenerateRequestDTO dictDizeGenerateRequestDTO) {

		new ThreadUtil().execAsync(() -> {
			List<String> wordList = getWordList(dictDizeGenerateRequestDTO.getPrompt(), dictDizeGenerateRequestDTO.getNum());
			DictBiz dictBizOne = dictBizService.lambdaQuery().eq(DictBiz::getId, dictDizeGenerateRequestDTO.getParentId()).one();
			List<String> values = dictBizMapper.getDictValues(dictDizeGenerateRequestDTO.getParentId());
			ArrayList<DictBiz> dictBizs = new ArrayList<>();
			for (String word : wordList) {
				if (!values.contains(word)) {
					DictBiz dictBiz = new DictBiz();
					dictBiz.setDictValue(word);
					dictBiz.setDictKey(word);
					dictBiz.setCode(dictBizOne.getCode());
					dictBiz.setParentId(dictBizOne.getId());
					dictBiz.setTenantId(dictBizOne.getTenantId());
					dictBizs.add(dictBiz);
				}
			}

			dictBizService.saveBatch(dictBizs);
		});

		return true;
	}

	private List<String> getWordList(String prompt, int num) {

		List<String> wordCollect = new ArrayList<>();

		Boolean isBoolean = true;
		while (isBoolean) {
			CompletionBody completionBody = new CompletionBody();
			completionBody.setAppCode("UjxlqsbN");
			completionBody.setToken("Link_tYYkOAp6Mam364bJlHgRWAVEMcZiAYE8mmWc5l0vIQ");
			completionBody.setPrompt(prompt);
			String completions = linkAIService.getCompletions(completionBody);

			String[] words = completions.split(",");
			wordCollect.addAll(Arrays.stream(words).distinct().collect(Collectors.toList()));

			if (wordCollect.size() >= num) {
				isBoolean = false;
			}
		}
		return wordCollect;
	}
}

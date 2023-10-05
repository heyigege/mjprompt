package com.smart.modules.mjPrompt.service.impl;

import com.alibaba.fastjson.JSON;
import com.smart.core.cache.utils.CacheUtil;
import com.smart.core.mp.base.BaseEntity;
import com.smart.core.mp.base.BaseServiceImpl;
import com.smart.modules.mjPrompt.entity.MjAppEntity;
import com.smart.modules.mjPrompt.entity.MjPhraseEntity;
import com.smart.modules.mjPrompt.entity.MjPhraseExpandEntity;
import com.smart.modules.mjPrompt.mapper.MjPhraseMapper;
import com.smart.modules.mjPrompt.service.IMjAppService;
import com.smart.modules.mjPrompt.service.IMjPhraseExpandService;
import com.smart.modules.mjPrompt.service.IMjPhraseService;
import com.smart.modules.system.entity.DictBiz;
import com.smart.modules.system.service.IDictBizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.smart.common.constant.MjAppConstant.Mj_APPID_CACHE;
import static com.smart.common.constant.MjAppConstant.Mj_PHRASE_CACHE;

/**
 * 服务实现类
 *
 * @author SmartX
 */
@Service
public class MjPhraseServiceImpl extends BaseServiceImpl<MjPhraseMapper, MjPhraseEntity> implements IMjPhraseService {
	@Autowired
	private IMjPhraseExpandService mjPhraseExpandService;
	@Autowired
	private IDictBizService dictBizService;

	@Autowired
	private IMjAppService mjAppService;

	@Override
	public String handlePhraseCache(String appCode) {

		MjAppEntity mjApp = mjAppService.lambdaQuery().eq(MjAppEntity::getAppCode, appCode).eq(MjAppEntity::getStatus, 1).one();

		MjPhraseEntity phrase = this.lambdaQuery().eq(MjPhraseEntity::getAppId, mjApp.getId()).eq(MjPhraseEntity::getStatus, 1).last("limit 1").one();

		List<MjPhraseExpandEntity> list = mjPhraseExpandService.lambdaQuery().eq(MjPhraseExpandEntity::getPhraseId, phrase.getId()).select(BaseEntity::getId, MjPhraseExpandEntity::getDictbizId, MjPhraseExpandEntity::getMaxNum, MjPhraseExpandEntity::getMinNum).list();
		ArrayList<Map> mapList = new ArrayList<>();
		for (MjPhraseExpandEntity expand : list) {
			HashMap expandMap = new HashMap<>();

			ArrayList<Long> parentIdList = Arrays.stream(Arrays.stream(expand.getDictbizId().split(",")).toArray()).map(it -> Long.valueOf((String) it)).collect(Collectors.toCollection(ArrayList::new));

			List<DictBiz> leafNodesList = dictBizService.getLeafNodesList(parentIdList);

			List<String> keyList = leafNodesList.stream().map(dictBiz -> dictBiz.getDictKey()).collect(Collectors.toList());
			keyList = keyList.stream().distinct().collect(Collectors.toList());
			expandMap.put("keyList", keyList);
			expandMap.put("minNum", expand.getMinNum());
			expandMap.put("maxNum", expand.getMaxNum());
			mapList.add(expandMap);

		}
		String s = JSON.toJSONString(mapList);
		System.out.println(s);
		return s;
	}
}

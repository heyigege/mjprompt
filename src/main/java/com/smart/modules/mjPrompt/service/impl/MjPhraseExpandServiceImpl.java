package com.smart.modules.mjPrompt.service.impl;

import com.smart.core.cache.utils.CacheUtil;
import com.smart.core.mp.base.BaseServiceImpl;
import com.smart.modules.mjPrompt.entity.MjPhraseExpandEntity;
import com.smart.modules.mjPrompt.mapper.MjPhraseExpandMapper;
import com.smart.modules.mjPrompt.service.IMjPhraseExpandService;
import org.springframework.stereotype.Service;

import static com.smart.common.constant.MjAppConstant.Mj_APPID_CACHE;
import static com.smart.common.constant.MjAppConstant.Mj_PHRASE_CACHE;

/**
 * 服务实现类
 *
 * @author SmartX
 */
@Service
public class MjPhraseExpandServiceImpl extends BaseServiceImpl<MjPhraseExpandMapper, MjPhraseExpandEntity> implements IMjPhraseExpandService {


}

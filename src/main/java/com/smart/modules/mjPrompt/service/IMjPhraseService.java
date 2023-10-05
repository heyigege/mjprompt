package com.smart.modules.mjPrompt.service;

import com.smart.core.mp.base.BaseService;
import com.smart.modules.mjPrompt.entity.MjPhraseEntity;

/**
 *  服务类
 *
 * @author SmartX
 */
public interface IMjPhraseService extends BaseService<MjPhraseEntity> {



	String handlePhraseCache(String appCode);

}

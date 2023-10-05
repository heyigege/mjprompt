package com.smart.modules.mjPrompt.wrapper;

import com.smart.core.mp.support.BaseEntityWrapper;
import com.smart.core.tool.utils.BeanUtil;
import com.smart.modules.mjPrompt.entity.MjPhraseEntity;
import com.smart.modules.mjPrompt.vo.MjPhraseVO;

/**
 *  包装类,返回视图层所需的字段
 *
 * @author SmartX
 */
public class MjPhraseWrapper extends BaseEntityWrapper<MjPhraseEntity, MjPhraseVO>  {

	public static MjPhraseWrapper build() {
		return new MjPhraseWrapper();
 	}

	@Override
	public MjPhraseVO entityVO(MjPhraseEntity mj_phrase) {
		MjPhraseVO mj_phraseVO = BeanUtil.copy(mj_phrase, MjPhraseVO.class);

		return mj_phraseVO;
	}

}

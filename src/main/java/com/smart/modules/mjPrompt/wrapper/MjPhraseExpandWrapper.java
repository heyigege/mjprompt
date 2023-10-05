package com.smart.modules.mjPrompt.wrapper;

import com.smart.core.mp.support.BaseEntityWrapper;
import com.smart.core.tool.utils.BeanUtil;
import com.smart.modules.mjPrompt.entity.MjPhraseExpandEntity;
import com.smart.modules.mjPrompt.vo.MjPhraseExpandVO;

/**
 *  包装类,返回视图层所需的字段
 *
 * @author SmartX
 */
public class MjPhraseExpandWrapper extends BaseEntityWrapper<MjPhraseExpandEntity, MjPhraseExpandVO>  {

	public static MjPhraseExpandWrapper build() {
		return new MjPhraseExpandWrapper();
 	}

	@Override
	public MjPhraseExpandVO entityVO(MjPhraseExpandEntity mj_phrase_expand) {
		MjPhraseExpandVO mj_phrase_expandVO = BeanUtil.copy(mj_phrase_expand, MjPhraseExpandVO.class);

		return mj_phrase_expandVO;
	}

}

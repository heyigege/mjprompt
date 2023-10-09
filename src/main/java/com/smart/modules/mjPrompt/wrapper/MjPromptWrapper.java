package com.smart.modules.mjPrompt.wrapper;

import com.smart.core.mp.support.BaseEntityWrapper;
import com.smart.core.tool.utils.BeanUtil;
import com.smart.modules.mjPrompt.entity.MjPromptEntity;
import com.smart.modules.mjPrompt.vo.MjPromptVO;

/**
 *  包装类,返回视图层所需的字段
 *
 * @author SmartX
 */
public class MjPromptWrapper extends BaseEntityWrapper<MjPromptEntity, MjPromptVO>  {

	public static MjPromptWrapper build() {
		return new MjPromptWrapper();
 	}

	@Override
	public MjPromptVO entityVO(MjPromptEntity mj_prompt) {
		MjPromptVO mj_promptVO = BeanUtil.copy(mj_prompt, MjPromptVO.class);

		//User createUser = UserCache.getUser(mj_prompt.getCreateUser());
		//User updateUser = UserCache.getUser(mj_prompt.getUpdateUser());
		//mj_promptVO.setCreateUserName(createUser.getName());
		//mj_promptVO.setUpdateUserName(updateUser.getName());

		return mj_promptVO;
	}

}

package com.smart.modules.mjPrompt.wrapper;

import com.smart.core.mp.support.BaseEntityWrapper;
import com.smart.core.tool.utils.BeanUtil;
import com.smart.modules.mjPrompt.entity.MjAppEntity;
import com.smart.modules.mjPrompt.vo.MjAppVO;

/**
 *  包装类,返回视图层所需的字段
 *
 * @author SmartX
 */
public class MjAppWrapper extends BaseEntityWrapper<MjAppEntity, MjAppVO>  {

	public static MjAppWrapper build() {
		return new MjAppWrapper();
 	}

	@Override
	public MjAppVO entityVO(MjAppEntity mj_app) {
		MjAppVO mj_appVO = BeanUtil.copy(mj_app, MjAppVO.class);

		//User createUser = UserCache.getUser(mj_app.getCreateUser());
		//User updateUser = UserCache.getUser(mj_app.getUpdateUser());
		//mj_appVO.setCreateUserName(createUser.getName());
		//mj_appVO.setUpdateUserName(updateUser.getName());

		return mj_appVO;
	}

}

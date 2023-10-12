package com.smart.modules.mjPrompt.wrapper;

import com.smart.core.mp.support.BaseEntityWrapper;
import com.smart.core.tool.utils.BeanUtil;
import com.smart.modules.mjPrompt.entity.MjAppConfigEntity;
import com.smart.modules.mjPrompt.vo.MjAppConfigVO;

/**
 *  包装类,返回视图层所需的字段
 *
 * @author SmartX
 */
public class MjAppConfigWrapper extends BaseEntityWrapper<MjAppConfigEntity, MjAppConfigVO>  {

	public static MjAppConfigWrapper build() {
		return new MjAppConfigWrapper();
 	}

	@Override
	public MjAppConfigVO entityVO(MjAppConfigEntity mjjAppConfig) {
		MjAppConfigVO mjjAppConfigVO = BeanUtil.copy(mjjAppConfig, MjAppConfigVO.class);

		//User createUser = UserCache.getUser(mjjAppConfig.getCreateUser());
		//User updateUser = UserCache.getUser(mjjAppConfig.getUpdateUser());
		//mjjAppConfigVO.setCreateUserName(createUser.getName());
		//mjjAppConfigVO.setUpdateUserName(updateUser.getName());

		return mjjAppConfigVO;
	}

}

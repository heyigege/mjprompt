/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package com.smart.modules.desk.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.smart.modules.desk.entity.Notice;
import com.smart.modules.desk.mapper.NoticeMapper;
import com.smart.modules.desk.service.INoticeService;
import com.smart.modules.desk.vo.NoticeVO;
import com.smart.core.mp.base.BaseServiceImpl;
import com.smart.core.secure.utils.AuthUtil;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
public class NoticeServiceImpl extends BaseServiceImpl<NoticeMapper, Notice> implements INoticeService {

	@Override
	public IPage<NoticeVO> selectNoticePage(IPage<NoticeVO> page, NoticeVO notice) {
		// 若不使用mybatis-plus自带的分页方法，则不会自动带入tenantId，所以我们需要自行注入
		notice.setTenantId(AuthUtil.getTenantId());
		return page.setRecords(baseMapper.selectNoticePage(page, notice));
	}

}

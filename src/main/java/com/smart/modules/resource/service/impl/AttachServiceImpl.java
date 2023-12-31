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
package com.smart.modules.resource.service.impl;

import com.smart.modules.resource.entity.Attach;
import com.smart.modules.resource.mapper.AttachMapper;
import com.smart.modules.resource.service.IAttachService;
import com.smart.modules.resource.vo.AttachVO;
import com.smart.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 附件表 服务实现类
 *
 * @author Chill
 */
@Service
public class AttachServiceImpl extends BaseServiceImpl<AttachMapper, Attach> implements IAttachService {

	@Override
	public IPage<AttachVO> selectAttachPage(IPage<AttachVO> page, AttachVO attach) {
		return page.setRecords(baseMapper.selectAttachPage(page, attach));
	}

}

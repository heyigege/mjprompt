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
package com.smart.modules.system.service;

import com.smart.modules.system.entity.Post;
import com.smart.modules.system.vo.PostVO;
import com.smart.core.mp.base.BaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 岗位表 服务类
 *
 * @author Chill
 */
public interface IPostService extends BaseService<Post> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param post
	 * @return
	 */
	IPage<PostVO> selectPostPage(IPage<PostVO> page, PostVO post);

	/**
	 * 获取岗位ID
	 *
	 * @param tenantId
	 * @param postNames
	 * @return
	 */
	String getPostIds(String tenantId, String postNames);

	/**
	 * 获取岗位ID
	 *
	 * @param tenantId
	 * @param postNames
	 * @return
	 */
	String getPostIdsByFuzzy(String tenantId, String postNames);

	/**
	 * 获取岗位名
	 *
	 * @param postIds
	 * @return
	 */
	List<String> getPostNames(String postIds);

}

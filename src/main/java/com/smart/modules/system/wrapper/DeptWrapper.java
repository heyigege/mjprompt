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
package com.smart.modules.system.wrapper;

import com.smart.common.cache.DictCache;
import com.smart.common.cache.SysCache;
import com.smart.common.enums.DictEnum;
import com.smart.core.mp.support.BaseEntityWrapper;
import com.smart.core.tool.constant.SmartConstant;
import com.smart.core.tool.node.ForestNodeMerger;
import com.smart.core.tool.utils.BeanUtil;
import com.smart.core.tool.utils.Func;
import com.smart.modules.system.entity.Dept;
import com.smart.modules.system.vo.DeptVO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 包装类,返回视图层所需的字段
 *
 * @author Chill
 */
public class DeptWrapper extends BaseEntityWrapper<Dept, DeptVO> {

	public static DeptWrapper build() {
		return new DeptWrapper();
	}

	@Override
	public DeptVO entityVO(Dept dept) {
		DeptVO deptVO = Objects.requireNonNull(BeanUtil.copy(dept, DeptVO.class));
		if (Func.equals(dept.getParentId(), SmartConstant.TOP_PARENT_ID)) {
			deptVO.setParentName(SmartConstant.TOP_PARENT_NAME);
		} else {
			Dept parent = SysCache.getDept(dept.getParentId());
			deptVO.setParentName(parent.getDeptName());
		}
		String category = DictCache.getValue(DictEnum.ORG_CATEGORY, dept.getDeptCategory());
		deptVO.setDeptCategoryName(category);
		return deptVO;
	}

	public List<DeptVO> listNodeVO(List<Dept> list) {
		List<DeptVO> collect = list.stream().map(dept -> {
			DeptVO deptVO = BeanUtil.copy(dept, DeptVO.class);
			String category = DictCache.getValue(DictEnum.ORG_CATEGORY, dept.getDeptCategory());
			Objects.requireNonNull(deptVO).setDeptCategoryName(category);
			return deptVO;
		}).collect(Collectors.toList());
		return ForestNodeMerger.merge(collect);
	}

	public List<DeptVO> listNodeLazyVO(List<DeptVO> list) {
		List<DeptVO> collect = list.stream().peek(dept -> {
			String category = DictCache.getValue(DictEnum.ORG_CATEGORY, dept.getDeptCategory());
			Objects.requireNonNull(dept).setDeptCategoryName(category);
		}).collect(Collectors.toList());
		return ForestNodeMerger.merge(collect);
	}

}

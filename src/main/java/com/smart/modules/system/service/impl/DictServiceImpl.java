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
package com.smart.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.common.cache.DictCache;
import com.smart.common.constant.CommonConstant;
import com.smart.core.cache.utils.CacheUtil;
import com.smart.core.log.exception.ServiceException;
import com.smart.core.mp.support.Condition;
import com.smart.core.mp.support.Query;
import com.smart.core.tool.constant.SmartConstant;
import com.smart.core.tool.node.ForestNodeMerger;
import com.smart.core.tool.utils.Func;
import com.smart.core.tool.utils.StringPool;
import com.smart.modules.system.entity.Dict;
import com.smart.modules.system.mapper.DictMapper;
import com.smart.modules.system.service.IDictService;
import com.smart.modules.system.vo.DictVO;
import com.smart.modules.system.wrapper.DictWrapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.smart.core.cache.constant.CacheConstant.DICT_CACHE;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {

	@Override
	public IPage<DictVO> selectDictPage(IPage<DictVO> page, DictVO dict) {
		return page.setRecords(baseMapper.selectDictPage(page, dict));
	}

	@Override
	public List<DictVO> tree() {
		return ForestNodeMerger.merge(baseMapper.tree());
	}

	@Override
	public List<DictVO> parentTree() {
		return ForestNodeMerger.merge(baseMapper.parentTree());
	}

	@Override
	public String getValue(String code, String dictKey) {
		return Func.toStr(baseMapper.getValue(code, dictKey), StringPool.EMPTY);
	}

	@Override
	public List<Dict> getList(String code) {
		return baseMapper.getList(code);
	}

	@Override
	public boolean submit(Dict dict) {
		LambdaQueryWrapper<Dict> lqw = Wrappers.<Dict>query().lambda().eq(Dict::getCode, dict.getCode()).eq(Dict::getDictKey, dict.getDictKey());
		Integer cnt = baseMapper.selectCount((Func.isEmpty(dict.getId())) ? lqw : lqw.notIn(Dict::getId, dict.getId()));
		if (cnt > 0) {
			throw new ServiceException("当前字典键值已存在!");
		}
		// 修改顶级字典后同步更新下属字典的编号
		if (Func.isNotEmpty(dict.getId()) && dict.getParentId().longValue() == SmartConstant.TOP_PARENT_ID) {
			Dict parent = DictCache.getById(dict.getId());
			this.update(Wrappers.<Dict>update().lambda().set(Dict::getCode, dict.getCode()).eq(Dict::getCode, parent.getCode()).ne(Dict::getParentId, SmartConstant.TOP_PARENT_ID));
		}
		if (Func.isEmpty(dict.getParentId())) {
			dict.setParentId(SmartConstant.TOP_PARENT_ID);
		}
		dict.setIsDeleted(SmartConstant.DB_NOT_DELETED);
		CacheUtil.clear(DICT_CACHE, Boolean.FALSE);
		return saveOrUpdate(dict);
	}

	@Override
	public boolean removeDict(String ids) {
		Integer cnt = baseMapper.selectCount(Wrappers.<Dict>query().lambda().in(Dict::getParentId, Func.toLongList(ids)));
		if (cnt > 0) {
			throw new ServiceException("请先删除子节点!");
		}
		return removeByIds(Func.toLongList(ids));
	}

	@Override
	public IPage<DictVO> parentList(Map<String, Object> dict, Query query) {
		IPage<Dict> page = this.page(Condition.getPage(query), Condition.getQueryWrapper(dict, Dict.class).lambda().eq(Dict::getParentId, CommonConstant.TOP_PARENT_ID).orderByAsc(Dict::getSort));
		return DictWrapper.build().pageVO(page);
	}

	@Override
	public List<DictVO> childList(Map<String, Object> dict, Long parentId) {
		if (parentId < 0) {
			return new ArrayList<>();
		}
		dict.remove("parentId");
		Dict parentDict = DictCache.getById(parentId);
		List<Dict> list = this.list(Condition.getQueryWrapper(dict, Dict.class).lambda().ne(Dict::getId, parentId).eq(Dict::getCode, parentDict.getCode()).orderByAsc(Dict::getSort));
		return DictWrapper.build().listNodeVO(list);
	}

	@Override
	public List<DictVO> childAllList(Map<String, Object> dict, Long parentId) {
		if (parentId < 0) {
			return new ArrayList<>();
		}
		Dict parentDict = DictCache.getById(parentId);
		List<Dict> list = this.list(Condition.getQueryWrapper(dict, Dict.class).lambda().ne(Dict::getId, parentId).eq(Dict::getCode, parentDict.getCode()).orderByAsc(Dict::getSort));
		return DictWrapper.build().listNodeVO(list);
	}


}

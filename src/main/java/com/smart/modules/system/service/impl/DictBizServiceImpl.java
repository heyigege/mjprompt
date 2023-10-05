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
import com.smart.common.cache.DictBizCache;
import com.smart.common.constant.CommonConstant;
import com.smart.modules.system.entity.DictBiz;
import com.smart.modules.system.mapper.DictBizMapper;
import com.smart.modules.system.service.IDictBizService;
import com.smart.modules.system.vo.DictBizVO;
import com.smart.modules.system.wrapper.DictBizWrapper;
import com.smart.core.cache.utils.CacheUtil;
import com.smart.core.log.exception.ServiceException;
import com.smart.core.mp.support.Condition;
import com.smart.core.mp.support.Query;
import com.smart.core.tool.constant.SmartConstant;
import com.smart.core.tool.node.ForestNodeMerger;
import com.smart.core.tool.utils.Func;
import com.smart.core.tool.utils.StringPool;
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
public class DictBizServiceImpl extends ServiceImpl<DictBizMapper, DictBiz> implements IDictBizService {

	@Override
	public List<DictBizVO> tree() {
		return ForestNodeMerger.merge(baseMapper.tree());
	}

	@Override
	public List<DictBizVO> parentTree() {
		return ForestNodeMerger.merge(baseMapper.parentTree());
	}

	@Override
	public String getValue(String code, String dictKey) {
		return Func.toStr(baseMapper.getValue(code, dictKey), StringPool.EMPTY);
	}

	@Override
	public List<DictBiz> getList(String code) {
		return baseMapper.getList(code);
	}

	@Override
	public boolean submit(DictBiz dict) {
		LambdaQueryWrapper<DictBiz> lqw = Wrappers.<DictBiz>query().lambda().eq(DictBiz::getCode, dict.getCode()).eq(DictBiz::getDictKey, dict.getDictKey());
		Integer cnt = baseMapper.selectCount((Func.isEmpty(dict.getId())) ? lqw : lqw.notIn(DictBiz::getId, dict.getId()));
		if (cnt > 0) {
			throw new ServiceException("当前字典键值已存在!");
		}
		// 修改顶级字典后同步更新下属字典的编号
		if (Func.isNotEmpty(dict.getId()) && dict.getParentId().longValue() == SmartConstant.TOP_PARENT_ID) {
			DictBiz parent = DictBizCache.getById(dict.getId());
			this.update(Wrappers.<DictBiz>update().lambda().set(DictBiz::getCode, dict.getCode()).eq(DictBiz::getCode, parent.getCode()).ne(DictBiz::getParentId, SmartConstant.TOP_PARENT_ID));
		}
		if (Func.isEmpty(dict.getParentId())) {
			dict.setParentId(SmartConstant.TOP_PARENT_ID);
		}
		dict.setIsDeleted(SmartConstant.DB_NOT_DELETED);
		CacheUtil.clear(DICT_CACHE);
		return saveOrUpdate(dict);
	}

	@Override
	public boolean removeDict(String ids) {
		Integer cnt = baseMapper.selectCount(Wrappers.<DictBiz>query().lambda().in(DictBiz::getParentId, Func.toLongList(ids)));
		if (cnt > 0) {
			throw new ServiceException("请先删除子节点!");
		}
		return removeByIds(Func.toLongList(ids));
	}

	@Override
	public IPage<DictBizVO> parentList(Map<String, Object> dict, Query query) {
		IPage<DictBiz> page = this.page(Condition.getPage(query), Condition.getQueryWrapper(dict, DictBiz.class).lambda().eq(DictBiz::getParentId, CommonConstant.TOP_PARENT_ID).orderByAsc(DictBiz::getSort));
		return DictBizWrapper.build().pageVO(page);
	}

	@Override
	public List<DictBizVO> childList(Map<String, Object> dict, Long parentId) {
		if (parentId < 0) {
			return new ArrayList<>();
		}
		dict.remove("parentId");
		DictBiz parentDict = DictBizCache.getById(parentId);
		List<DictBiz> list = this.list(Condition.getQueryWrapper(dict, DictBiz.class).lambda().ne(DictBiz::getId, parentId).eq(DictBiz::getCode, parentDict.getCode()).orderByAsc(DictBiz::getSort));
		return DictBizWrapper.build().listNodeVO(list);
	}

	@Override
	public List<DictBizVO> getChildTree(Long parentId) {
		//根据传递进来的parentId递归出所有的子级
		List<DictBiz> list = this.list(Wrappers.<DictBiz>query().lambda().eq(DictBiz::getParentId, parentId).orderByAsc(DictBiz::getSort));
		if (list == null || list.size() == 0) {
			return null;
		}
		List<DictBizVO> dictBizVO = DictBizWrapper.build().listNodeVO(list);
		//遍历所有的子级
		for (DictBizVO dictBiz : dictBizVO) {
			List<DictBizVO> childTree = getChildTree(dictBiz.getId());
			if (childTree != null) {
				dictBiz.setChildren(childTree);
			}
		}
		return dictBizVO;
	}

	@Override
	public List<DictBiz> getLeafNodesList(List<Long> parentIdList) {
		List<DictBiz> result = new ArrayList<>();
		for (Long parentId : parentIdList) {
			recursivelyFetchLeafNodes(parentId, result);
		}
		return result;
	}

	private void recursivelyFetchLeafNodes(Long parentId, List<DictBiz> result) {
		List<DictBiz> children = this.lambdaQuery().eq(DictBiz::getParentId, parentId).list();
		DictBiz biz = this.lambdaQuery().eq(DictBiz::getId, parentId).one();
		result.add(biz);
		for (DictBiz child : children) {
			List<DictBiz> grandChildren = this.lambdaQuery().eq(DictBiz::getParentId, child.getId()).list();
			if (grandChildren.isEmpty()) {
				result.add(child);
			} else {
				result.add(this.lambdaQuery().eq(DictBiz::getId, child.getId()).one());
				recursivelyFetchLeafNodes(child.getId(), result);
			}
		}
	}
}

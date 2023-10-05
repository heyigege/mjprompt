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

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.common.cache.RegionCache;
import com.smart.modules.system.entity.Region;
import com.smart.modules.system.excel.RegionExcel;
import com.smart.modules.system.mapper.RegionMapper;
import com.smart.modules.system.service.IRegionService;
import com.smart.modules.system.vo.RegionVO;
import com.smart.core.log.exception.ServiceException;
import com.smart.core.tool.utils.BeanUtil;
import com.smart.core.tool.utils.Func;
import com.smart.core.tool.utils.StringPool;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 行政区划表 服务实现类
 *
 * @author Chill
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements IRegionService {

	@Override
	public boolean submit(Region region) {
		Integer cnt = baseMapper.selectCount(Wrappers.<Region>query().lambda().eq(Region::getCode, region.getCode()));
		if (cnt > 0) {
			return this.updateById(region);
		}
		// 设置祖区划编号
		Region parent = RegionCache.getByCode(region.getParentCode());
		if (Func.isNotEmpty(parent) || Func.isNotEmpty(parent.getCode())) {
			String ancestors = parent.getAncestors() + StringPool.COMMA + parent.getCode();
			region.setAncestors(ancestors);
		}
		// 设置省、市、区、镇、村
		Integer level = region.getRegionLevel();
		String code = region.getCode();
		String name = region.getName();
		if (level == RegionCache.PROVINCE_LEVEL) {
			region.setProvinceCode(code);
			region.setProvinceName(name);
		} else if (level == RegionCache.CITY_LEVEL) {
			region.setCityCode(code);
			region.setCityName(name);
		} else if (level == RegionCache.DISTRICT_LEVEL) {
			region.setDistrictCode(code);
			region.setDistrictName(name);
		} else if (level == RegionCache.TOWN_LEVEL) {
			region.setTownCode(code);
			region.setTownName(name);
		} else if (level == RegionCache.VILLAGE_LEVEL) {
			region.setVillageCode(code);
			region.setVillageName(name);
		}
		return this.save(region);
	}

	@Override
	public boolean removeRegion(String id) {
		Integer cnt = baseMapper.selectCount(Wrappers.<Region>query().lambda().eq(Region::getParentCode, id));
		if (cnt > 0) {
			throw new ServiceException("请先删除子节点!");
		}
		return removeById(id);
	}

	@Override
	public List<RegionVO> lazyList(String parentCode, Map<String, Object> param) {
		return baseMapper.lazyList(parentCode, param);
	}

	@Override
	public List<RegionVO> lazyTree(String parentCode, Map<String, Object> param) {
		return baseMapper.lazyTree(parentCode, param);
	}

	@Override
	public void importRegion(List<RegionExcel> data, Boolean isCovered) {
		List<Region> list = new ArrayList<>();
		data.forEach(regionExcel -> {
			Region region = BeanUtil.copy(regionExcel, Region.class);
			list.add(region);
		});
		if (isCovered) {
			this.saveOrUpdateBatch(list);
		} else {
			this.saveBatch(list);
		}
	}

	@Override
	public List<RegionExcel> exportRegion(Wrapper<Region> queryWrapper) {
		return baseMapper.exportRegion(queryWrapper);
	}
}

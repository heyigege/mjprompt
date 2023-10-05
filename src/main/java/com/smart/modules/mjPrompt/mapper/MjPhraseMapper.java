package com.smart.modules.mjPrompt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smart.modules.mjPrompt.entity.MjPhraseEntity;
import org.apache.ibatis.annotations.Param;

/**
 *  Mapper 接口
 *
 * @author SmartX
 */
public interface MjPhraseMapper extends BaseMapper<MjPhraseEntity> {

	String getPhraseSuffixByAppCode(@Param("appCode") String appCode);

}

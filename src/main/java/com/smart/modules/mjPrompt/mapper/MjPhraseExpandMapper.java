package com.smart.modules.mjPrompt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smart.modules.mjPrompt.entity.MjPhraseExpandEntity;
import org.apache.ibatis.annotations.Param;

/**
 * Mapper 接口
 *
 * @author SmartX
 */
public interface MjPhraseExpandMapper extends BaseMapper<MjPhraseExpandEntity> {

	String getAppCode(@Param("phraseId") Long phraseId);
}

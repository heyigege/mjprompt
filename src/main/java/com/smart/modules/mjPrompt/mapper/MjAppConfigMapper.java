package com.smart.modules.mjPrompt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smart.modules.mjPrompt.dto.MjAppConfigDTO;
import com.smart.modules.mjPrompt.entity.MjAppConfigEntity;
import org.apache.ibatis.annotations.Param;

/**
 * Mapper 接口
 *
 * @author SmartX
 */
public interface MjAppConfigMapper extends BaseMapper<MjAppConfigEntity> {

	MjAppConfigDTO getAppConfig(@Param("appCode") String appCode);
}

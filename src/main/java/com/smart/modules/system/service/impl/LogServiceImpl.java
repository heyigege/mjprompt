package com.smart.modules.system.service.impl;

import com.smart.modules.system.service.ILogApiService;
import com.smart.modules.system.service.ILogErrorService;
import com.smart.modules.system.service.ILogService;
import com.smart.modules.system.service.ILogUsualService;
import lombok.AllArgsConstructor;
import com.smart.core.log.model.LogApi;
import com.smart.core.log.model.LogError;
import com.smart.core.log.model.LogUsual;
import org.springframework.stereotype.Service;

/**
 * Created by Smart.
 *
 * @author zhuangqian
 */
@Service
@AllArgsConstructor
public class LogServiceImpl implements ILogService {

	private final ILogUsualService usualService;
	private final ILogApiService apiService;
	private final ILogErrorService errorService;

	@Override
	public Boolean saveUsualLog(LogUsual log) {
		return usualService.save(log);
	}

	@Override
	public Boolean saveApiLog(LogApi log) {
		return apiService.save(log);
	}

	@Override
	public Boolean saveErrorLog(LogError log) {
		return errorService.save(log);
	}

}

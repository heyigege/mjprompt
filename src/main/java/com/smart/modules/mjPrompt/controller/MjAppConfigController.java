package com.smart.modules.mjPrompt.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.gson.Gson;
import com.smart.core.boot.ctrl.SmartController;
import com.smart.core.mp.support.Condition;
import com.smart.core.mp.support.Query;
import com.smart.core.tool.api.R;
import com.smart.core.tool.utils.BeanUtil;
import com.smart.core.tool.utils.Func;
import com.smart.modules.mjPrompt.dto.MjAppConfigDTO;
import com.smart.modules.mjPrompt.entity.MjAppConfigEntity;
import com.smart.modules.mjPrompt.service.IMjAppConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;


/**
 * 控制器
 *
 * @author SmartX
 */
@RestController
@AllArgsConstructor
@RequestMapping("/smart-mj/mjjAppConfig")
@Api(value = "", tags = "")
public class MjAppConfigController extends SmartController {

	private final IMjAppConfigService mjjAppConfigService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入mjjAppConfig")
	public R<MjAppConfigDTO> detail(MjAppConfigEntity mjjAppConfig) {
		MjAppConfigEntity detail = mjjAppConfigService.getOne(Condition.getQueryWrapper(mjjAppConfig));
		MjAppConfigDTO mjAppConfigDTO = new MjAppConfigDTO();
		BeanUtil.copyProperties(detail, mjAppConfigDTO);
		if (detail.getEmailConfig() != null) {
			Gson gson = new Gson();
			HashMap<String, String> emailConfigMap = gson.fromJson(detail.getEmailConfig(), HashMap.class);
			mjAppConfigDTO.setMailSender(emailConfigMap.get("mailSender"));
			mjAppConfigDTO.setMailSenderName(emailConfigMap.get("mailSenderName"));
			mjAppConfigDTO.setMailRecipient(emailConfigMap.get("mailRecipient"));
			mjAppConfigDTO.setMailSubject(emailConfigMap.get("mailSubject"));
			mjAppConfigDTO.setMailBody(emailConfigMap.get("mailBody"));
			mjAppConfigDTO.setSmtpServer(emailConfigMap.get("smtpServer"));
			mjAppConfigDTO.setSmtpUsername(emailConfigMap.get("smtpUsername"));
			mjAppConfigDTO.setSmtpPassword(emailConfigMap.get("smtpPassword"));
		}

		return R.data(mjAppConfigDTO);
	}

	/**
	 * 分页 代码自定义代号
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入mjjAppConfig")
	public R<IPage<MjAppConfigEntity>> list(MjAppConfigEntity mjjAppConfig, Query query) {
		IPage<MjAppConfigEntity> pages = mjjAppConfigService.page(Condition.getPage(query), Condition.getQueryWrapper(mjjAppConfig));
		return R.data(pages);
	}

	/**
	 * 新增 代码自定义代号
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入mjjAppConfig")
	public R save(@Valid @RequestBody MjAppConfigEntity mjjAppConfig) {
		return R.status(mjjAppConfigService.save(mjjAppConfig));
	}

	/**
	 * 修改 代码自定义代号
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入mjjAppConfig")
	public R update(@Valid @RequestBody MjAppConfigDTO mjAppConfigDTO) {
		MjAppConfigEntity mjAppConfigEntity = new MjAppConfigEntity();
		BeanUtil.copyProperties(mjAppConfigDTO, mjAppConfigEntity);
		HashMap<String, String> mailMap = new HashMap<>();
		mailMap.put("mailSender", mjAppConfigDTO.getMailSender());
		mailMap.put("mailSenderName", mjAppConfigDTO.getMailSenderName());
		mailMap.put("mailRecipient", mjAppConfigDTO.getMailRecipient());
		mailMap.put("mailSubject", mjAppConfigDTO.getMailSubject());
		mailMap.put("mailBody", mjAppConfigDTO.getMailBody());
		mailMap.put("smtpServer", mjAppConfigDTO.getSmtpServer());
		mailMap.put("smtpUsername", mjAppConfigDTO.getSmtpUsername());
		mailMap.put("smtpPassword", mjAppConfigDTO.getSmtpPassword());

		Gson gson = new Gson();
		String mailString = gson.toJson(mailMap);
		mjAppConfigEntity.setEmailConfig(mailString);

		return R.status(mjjAppConfigService.updateById(mjAppConfigEntity));
	}

	/**
	 * 新增或修改 代码自定义代号
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入mjjAppConfig")
	public R submit(@Valid @RequestBody MjAppConfigEntity mjjAppConfig) {
		return R.status(mjjAppConfigService.saveOrUpdate(mjjAppConfig));
	}


	/**
	 * 删除 代码自定义代号
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(mjjAppConfigService.deleteLogic(Func.toLongList(ids)));
	}

}

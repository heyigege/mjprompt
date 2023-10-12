package com.smart.modules.mjPrompt.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.smart.modules.mjPrompt.entity.MjAppConfigEntity;

/**
 * 模型DTO
 *
 * @author SmartX
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MjAppConfigDTO extends MjAppConfigEntity {

	private static final long serialVersionUID = 1L;


	@ApiModelProperty(value = "发件人")
	private String mailSender;

	@ApiModelProperty(value = "发件人名称")
	private String mailSenderName;

	@ApiModelProperty(value = "收件人")
	private String mailRecipient;

	@ApiModelProperty(value = "主题")
	private String mailSubject;

	@ApiModelProperty(value = "正文")
	private String mailBody;

	@ApiModelProperty(value = "SMTP服务器")
	private String smtpServer;

	@ApiModelProperty(value = "SMTP用户名")
	private String smtpUsername;

	@ApiModelProperty(value = "SMTP密码")
	private String smtpPassword;
}

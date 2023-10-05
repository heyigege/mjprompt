package com.smart.modules.desk.vo;

import com.smart.modules.desk.entity.Notice;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通知公告视图类
 *
 * @author Chill
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NoticeVO extends Notice {

	@ApiModelProperty(value = "通知类型名")
	private String categoryName;

	@ApiModelProperty(value = "租户编号")
	private String tenantId;

}

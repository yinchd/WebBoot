package com.yinchd.web.dto.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * 分页请求
 * @author yinchd
 * @date 2019/9/19
*/
@Data
@ApiModel("请求入参")
public class PageRequest<T> {
	/**
	 * 当前页码
	 */
	@ApiModelProperty(example = "1", value = "当前页码，默认为1")
	@Range(min = 1, message = "页码范围有误")
	private int pageNum = 1;
	/**
	 * 每页数量
	 */
	@ApiModelProperty(example = "10", value = "每页数量，默认为10")
	@Range(min = 0, max = 5000, message = "每页数量有误")
	private int pageSize = 10;

	/**
	 * 排序字段
	 */
	@ApiModelProperty(value = "排序字段", example = "createTime")
	private String orderBy;

	/**
	 * 排序类型 desc asc
	 */
	@ApiModelProperty(value = "升序降序", example = "desc|asc")
	private String order;

	@ApiModelProperty(value = "请求参数")
	private T param;

}

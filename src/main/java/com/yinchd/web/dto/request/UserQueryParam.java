package com.yinchd.web.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yinchd
 * @version 1.0
 * @description 用户查询参数
 * @date 2021/3/16 17:24
 */
@Data
@ApiModel("用户列表查询参数")
public class UserQueryParam {

    @ApiModelProperty(value = "账号")
    String account;

    @ApiModelProperty(value = "用户性别（1男 2女）")
    private Integer sex;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "状态(0：禁用 1：正常)")
    private Integer status;

}

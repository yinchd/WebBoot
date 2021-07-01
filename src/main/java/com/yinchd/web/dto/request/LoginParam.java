package com.yinchd.web.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录对象
 * @author yinchd
 * @date 2019/9/20
 */
@Data
@ApiModel("登录对象")
public class LoginParam {

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String account;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;
    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String captcha;

}
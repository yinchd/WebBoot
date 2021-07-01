package com.yinchd.web.dto.request;

import com.yinchd.web.constants.RegexConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @author yinchd
 * @version 1.0
 * @description 新增用户封装参数
 * @date 2021/3/17 14:46
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "新增用户封装参数", description = "非必填的参数传到后台一律传null，否则会使用校验规则校验")
public class UserAddParam {

    @ApiModelProperty(value = "用户id，传值的话代表修改用户")
    Integer id;

    @NotBlank(message = "账号不能为空")
    @Pattern(regexp = RegexConstants.ACCOUNT_EN, message = "账户不符合要求")
    @ApiModelProperty(value = "账号，只可包含大、写英文字母、数字以及符号 _@. 长度为4~20位", required = true)
    String account;

    @NotBlank(message = "姓名不能为空")
    @Pattern(regexp = RegexConstants.USERNAME, message = "姓名不符合要求")
    @ApiModelProperty(value = "姓名，只可包含中、英文，长度为2~20位", required = true)
    String userName;

    @Pattern(regexp = RegexConstants.PASSWORD_8_16, message = "密码不符合要求")
    @ApiModelProperty(value = "密码，须包含大小写英文字母、数字，长度为8~16位")
    String password;

    @ApiModelProperty(value = "头像，文件中心上传后返回的完整路径")
    String avatar;

    @Range(min = 1, max = 2, message = "请输入正确的性别参数")
    @ApiModelProperty(value = "性别")
    Integer sex;

    @Email(message = "邮箱格式不符合要求")
    @ApiModelProperty(value = "邮箱")
    String email;

    @Pattern(regexp = RegexConstants.PHONE, message = "手机号不符合要求")
    @ApiModelProperty(value = "手机号")
    String mobile;

    @Pattern(regexp = RegexConstants.TELEPHONE, message = "座机号不符合要求")
    @ApiModelProperty(value = "座机")
    String telephone;

    @ApiModelProperty(value = "民族")
    String nation;

    @ApiModelProperty(value = "用户所属角色，传角色id")
    List<Integer> roles;
}

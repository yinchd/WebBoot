package com.yinchd.web.dto.request;

import com.yinchd.web.constants.RegexConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @author yinchd
 * @version 1.0
 * @description 新增角色封装参数
 * @date 2021/3/17 14:46
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "新增角色封装参数", description = "非必填的参数传到后台一律传null，否则会使用校验规则校验")
public class RoleAddParam {

    @ApiModelProperty(value = "角色id，传值的话代表修改角色")
    private Integer id;

    @ApiModelProperty(value = "角色名称，长度2~50位，可包含中英文字母及数字", required = true)
    @Pattern(regexp = RegexConstants.ROLE_NAME, message = "角色名称不符合要求")
    @NotBlank(message = "角色名称不能为空")
    private String name;

    @ApiModelProperty(value = "角色描述")
    @Pattern(regexp = "^[\\u4e00-\\u9fa50-9.a-zA-Z]{1,200}$", message = "角色描述可输入中英文、数字，长度为1~200个字符")
    private String note;

    @ApiModelProperty("关联菜单")
    private List<Integer> menus;

}

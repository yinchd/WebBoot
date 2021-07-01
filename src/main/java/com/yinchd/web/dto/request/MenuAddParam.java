package com.yinchd.web.dto.request;

import com.yinchd.web.constants.RegexConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author yinchd
 * @version 1.0
 * @description 新增菜单封装参数
 * @date 2021/3/17 14:46
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "新增菜单封装参数", description = "非必填的参数传到后台一律传null，否则会使用校验规则校验")
public class MenuAddParam {

    @ApiModelProperty(value = "菜单id，传值的话代表修改菜单")
    private Integer id;

    @ApiModelProperty(value = "父级菜单id，初次新增时，pid为0")
    @NotNull(message = "父级菜单不能为空")
    @Min(value = 0, message = "请输入正确的父级菜单id")
    private Integer pid;

    @ApiModelProperty(value = "菜单名称，长度2~20位，可包含中英文字母及下划线", required = true)
    @Pattern(regexp = RegexConstants.COMMON_NAME, message = "菜单名称不符合要求")
    @NotBlank(message = "菜单名称不能为空")
    private String name;

    @ApiModelProperty(value = "菜单类型（1目录 2链接 3按钮）", required = true)
    @Range(min = 1, max = 3, message = "type传值错误")
    @NotNull(message = "菜单类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "菜单图标，可用Font Awesome图标")
    @Pattern(regexp = "(^fa).*", message = "请传入正确的Font Awesome图标代码")
    private String icon;

    @ApiModelProperty(value = "层级，顶级菜单层级为0，子级菜单依次加1")
    @NotNull(message = "层级不能为空")
    @Min(value = 0, message = "请输入正确的层级")
    private Integer level;

    @ApiModelProperty(value = "菜单链接，目录默认传单斜杠：/", required = true)
    @Pattern(regexp = "/[\\w|/_-]*", message = "菜单链接不符合要求")
    @NotBlank(message = "菜单链接不能为空")
    private String link;

    @ApiModelProperty(value = "排序")
    @Min(value = 0, message = "排序值请输入大于0的参数")
    private Integer rank;
}

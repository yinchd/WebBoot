package com.yinchd.web.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yinchd
 * @version 1.0
 * @description 角色查询参数
 * @date 2021/3/16 17:24
 */
@Data
@ApiModel("角色列表查询参数")
public class RoleQueryParam {

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "菜单id集合")
    private List<Integer> menus;
}

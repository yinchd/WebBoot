package com.yinchd.web.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author yinchd
 * @version 1.0
 * @description 菜单树展示实体
 * @date 2021/3/18 11:17
 */
@Data
@Accessors(chain = true)
@ApiModel("菜单树展示实体")
public class MenuTree {

    @ApiModelProperty(value = "资源id")
    private Integer id;

    @ApiModelProperty(value = "父级id")
    private Integer pid;

    @ApiModelProperty(value = "菜单名称")
    private String name;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "菜单类型（1目录 2链接 3按钮）")
    private String type;

    @ApiModelProperty(value = "菜单链接")
    private String link;

    @ApiModelProperty(value = "排序")
    private Integer rank;

    @ApiModelProperty(value = "层级")
    private Integer level;

    @ApiModelProperty(value = "状态(0：禁用 1：正常)")
    private String status;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "子菜单")
    private List<MenuTree> child;
}

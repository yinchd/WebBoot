package com.yinchd.web.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yinchd
 * @version 1.0
 * @description 用户列表展示实体
 * @date 2021/3/16 17:27
 */
@Data
@ApiModel("用户列表展示实体")
public class RolePage {

    @ApiModelProperty(value = "角色id")
    private Integer id;

    @ApiModelProperty(value = "角色标识")
    private String name;

    @ApiModelProperty(value = "角色名称")
    private String alias;

    @ApiModelProperty(value = "角色状态")
    private String status;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "角色具有的菜单资源集合")
    private List<Integer> roleMenus;

}

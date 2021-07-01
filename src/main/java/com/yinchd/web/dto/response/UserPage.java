package com.yinchd.web.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yinchd
 * @version 1.0
 * @description 用户列表展示实体
 * @date 2021/3/16 17:27
 */
@Data
@ApiModel("用户列表展示实体")
public class UserPage {

    @ApiModelProperty(value = "用户id")
    private Integer id;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "用户性别")
    private String sex;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "座机号码")
    private String telephone;

    @ApiModelProperty(value = "民族")
    private String nation;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "所属部门")
    private String dept;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private String createTime;
}

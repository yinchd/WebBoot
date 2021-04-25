package com.yinchd.web.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户管理
 * </p>
 *
 * @author yinchd
 * @since 2021-04-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
@ApiModel(value="UserModel对象", description="用户管理")
public class UserModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编号")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "账号")
    @TableField("account")
    private String account;

    @ApiModelProperty(value = "姓名")
    @TableField("user_name")
    private String userName;

    @ApiModelProperty(value = "密码")
    @TableField("password")
    private String password;

    @ApiModelProperty(value = "头像")
    @TableField("avatar")
    private String avatar;

    @ApiModelProperty(value = "用户性别（1男 2女）")
    @TableField("sex")
    private Integer sex;

    @ApiModelProperty(value = "邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty(value = "手机号")
    @TableField("mobile")
    private String mobile;

    @ApiModelProperty(value = "座机号码")
    @TableField("telephone")
    private String telephone;

    @ApiModelProperty(value = "民族")
    @TableField("nation")
    private String nation;

    @ApiModelProperty(value = "状态(0：禁用 1：正常)")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "用户所属部门，多个部门id之间用|线分隔")
    @TableField("dept")
    private String dept;

    @ApiModelProperty(value = "创建人")
    @TableField("create_by")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    @TableField("last_update_by")
    private String lastUpdateBy;

    @ApiModelProperty(value = "更新时间")
    @TableField("last_update_time")
    private LocalDateTime lastUpdateTime;

    @ApiModelProperty(value = "逻辑删除（0未删，1已删）")
    @TableField("del_flag")
    private Integer delFlag;


    public static final String ID = "id";

    public static final String ACCOUNT = "account";

    public static final String USER_NAME = "user_name";

    public static final String PASSWORD = "password";

    public static final String AVATAR = "avatar";

    public static final String SEX = "sex";

    public static final String EMAIL = "email";

    public static final String MOBILE = "mobile";

    public static final String TELEPHONE = "telephone";

    public static final String NATION = "nation";

    public static final String STATUS = "status";

    public static final String DEPT = "dept";

    public static final String CREATE_BY = "create_by";

    public static final String CREATE_TIME = "create_time";

    public static final String LAST_UPDATE_BY = "last_update_by";

    public static final String LAST_UPDATE_TIME = "last_update_time";

    public static final String DEL_FLAG = "del_flag";

}

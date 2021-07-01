package com.yinchd.web.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author yinchd
 * @since 2021-04-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role")
@ApiModel(value="RoleModel对象", description="")
public class RoleModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色id")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "角色名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "角色描述")
    @TableField("note")
    private String note;

    @ApiModelProperty(value = "状态(0：禁用 1：正常)")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "创建人")
    @TableField("create_by")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改人")
    @TableField("last_update_by")
    private String lastUpdateBy;

    @ApiModelProperty(value = "修改时间")
    @TableField("last_update_time")
    private LocalDateTime lastUpdateTime;


    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String NOTE = "note";

    public static final String STATUS = "status";

    public static final String CREATE_BY = "create_by";

    public static final String CREATE_TIME = "create_time";

    public static final String LAST_UPDATE_BY = "last_update_by";

    public static final String LAST_UPDATE_TIME = "last_update_time";

}

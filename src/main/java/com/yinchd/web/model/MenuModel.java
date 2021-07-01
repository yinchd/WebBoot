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
 * @since 2021-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_menu")
@ApiModel(value="MenuModel对象", description="")
public class MenuModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "资源id")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "父级id")
    @TableField("pid")
    private Integer pid;

    @ApiModelProperty(value = "层级,顶级菜单层级为0")
    @TableField("level")
    private Integer level;

    @ApiModelProperty(value = "菜单名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "菜单图标")
    @TableField("icon")
    private String icon;

    @ApiModelProperty(value = "菜单类型（1目录 2链接 3按钮）")
    @TableField("type")
    private Integer type;

    @ApiModelProperty(value = "菜单链接")
    @TableField("link")
    private String link;

    @ApiModelProperty(value = "排序")
    @TableField("rank")
    private Integer rank;

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

    public static final String PID = "pid";

    public static final String LEVEL = "level";

    public static final String NAME = "name";

    public static final String ICON = "icon";

    public static final String TYPE = "type";

    public static final String LINK = "link";

    public static final String RANK = "rank";

    public static final String STATUS = "status";

    public static final String CREATE_BY = "create_by";

    public static final String CREATE_TIME = "create_time";

    public static final String LAST_UPDATE_BY = "last_update_by";

    public static final String LAST_UPDATE_TIME = "last_update_time";

}

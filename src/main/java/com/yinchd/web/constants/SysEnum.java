package com.yinchd.web.constants;

/**
 * @author yinchd
 * @version 1.0
 * @description 系统枚举类
 * @date 2021/3/17 12:08
 */
public enum SysEnum {

    /**
     * 逻辑删除状态
     */
    DEL_FLAG_VALID(0, "有效"),
    DEL_FLAG_INVALID(1, "无效"),

    /**
     * 数据（记录）状态
     */
    STATUS_FREEZE(0, "禁用"),
    STATUS_NORMAL(1, "正常"),
    /**
     * 性别
     */
    MALE(1, "男"),
    FEMALE(2, "女"),
    /**
     * 菜单类型
     */
    MENU_DIR(1, "目录"),
    MENU_LINK(2, "链接"),
    MENU_BTN(3, "按钮");

    /**
     * 枚举值
     */
    private int code;
    /**
     * 枚举名称
     */
    private String name;

    SysEnum (int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 根据状态编码获取状态名称
     * @param code 状态编码
     * @return 状态名称
     */
    public static String getStatus(int code) {
        return code == STATUS_FREEZE.code ? STATUS_FREEZE.name : STATUS_NORMAL.name;
    }
    /**
     * 根据性别编码获取性别名称
     * @param code 性别编码
     * @return 性别名称
     */
    public static String getSex(int code) {
        return code == MALE.code ? MALE.name : FEMALE.name;
    }

    /**
     * 获取菜单类型
     * @param code 菜单编码
     */
    public static String getMenuType(int code) {
        if (SysEnum.MENU_DIR.getCode() == code) {
            return SysEnum.MENU_DIR.name;
        } else if (SysEnum.MENU_LINK.getCode() == code) {
            return SysEnum.MENU_LINK.name;
        } else {
            return SysEnum.MENU_BTN.name;
        }
    }
}

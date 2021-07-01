package com.yinchd.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yinchd.web.dto.response.MenuTree;
import com.yinchd.web.model.RoleMenuModel;

import java.util.List;

/**
 * <p>
 * 角色菜单映射表 服务类
 * </p>
 *
 * @author yinchd
 * @since 2021-03-18
 */
public interface RoleMenuService extends IService<RoleMenuModel> {

    /**
     * 保存角色资源信息
     * @param roleId 角色id
     * @param menus 资源（菜单）集合
     */
    boolean saveRoleMenus(Integer roleId, List<Integer> menus);

    /**
     * 根据菜单查询出所有关联的角色信息
     * @param menuIds 菜单id集合
     */
    List<Integer> getRoleIdsByMenuIds(List<Integer> menuIds);

    /**
     * 获取角色所具有的菜单资源信息
     * @param roleIds 角色id
     */
    List<Integer> getRoleMenuIds(List<Integer> roleIds);

    /**
     * 获取角色所具有的菜单链接信息
     * @param roleIds 角色id
     */
    List<String> getRoleMenuLinks(List<Integer> roleIds);

    /**
     * 根据角色信息获取角色对应的菜单树
     * @param roleIds 角色id集合
     */
    List<MenuTree> getRoleMenuTree(List<Integer> roleIds);

    /**
     * 获取角色对应的所有菜单链接
     * @param roleIds 角色id集合
     */
    List<String> getRoleMenus(List<Integer> roleIds);
}

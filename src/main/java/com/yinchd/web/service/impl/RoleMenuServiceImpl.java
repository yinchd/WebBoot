package com.yinchd.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yinchd.web.constants.SysEnum;
import com.yinchd.web.dto.response.MenuTree;
import com.yinchd.web.mapper.RoleMenuMapper;
import com.yinchd.web.model.MenuModel;
import com.yinchd.web.model.RoleMenuModel;
import com.yinchd.web.service.MenuService;
import com.yinchd.web.service.RoleMenuService;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色菜单映射表 服务实现类
 * </p>
 *
 * @author yinchd
 * @since 2021-03-18
 */
@Service
@Slf4j
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenuModel> implements RoleMenuService {

    @Resource
    private MenuService menuService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRoleMenus(Integer roleId, List<Integer> menus) {
        Assert.notNull(roleId, "角色id不能为空");
        log.info("保存角色：{} 菜单资源信息：{}", roleId, menus);
        // 过滤出无效的菜单
        if (CollectionUtils.isNotEmpty(menus)) {
            List<MenuModel> menuList = menuService.getBaseMapper().selectBatchIds(menus);
            menus = menuList.stream().map(MenuModel::getId).collect(Collectors.toList());
        }

        // 如果之前有角色资源关联，则先删除再重新插入
        QueryWrapper<RoleMenuModel> qr = new QueryWrapper<>();
        qr.eq(RoleMenuModel.ROLE_ID, roleId);
        int delete = super.baseMapper.delete(qr);
        if (delete > 0) {
            log.info("对角色{}之前关联的菜单资源清除成功", roleId);
        }
        List<RoleMenuModel> adds = new ArrayList<>();
        for (Integer menuId : menus) {
            RoleMenuModel obj = new RoleMenuModel();
            obj.setRoleId(roleId);
            obj.setMenuId(menuId);
            adds.add(obj);
        }
        return super.saveBatch(adds);
    }

    @Override
    public List<Integer> getRoleIdsByMenuIds(List<Integer> menuIds) {
        Assert.notEmpty(menuIds, "菜单id不能为空");
        QueryWrapper<RoleMenuModel> qr = new QueryWrapper<>();
        qr.in(RoleMenuModel.MENU_ID, menuIds);
        return super.baseMapper.selectList(qr)
                .stream()
                .map(RoleMenuModel::getRoleId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> getRoleMenuIds(List<Integer> roleIds) {
        Assert.notEmpty(roleIds, "角色信息不能为空");
        QueryWrapper<RoleMenuModel> qr = new QueryWrapper<>();
        qr.in(RoleMenuModel.ROLE_ID, roleIds);
        return super.baseMapper.selectList(qr)
                .stream()
                .map(RoleMenuModel::getMenuId)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getRoleMenuLinks(List<Integer> roleIds) {
        Assert.notEmpty(roleIds, "角色信息不能为空");
        List<Integer> menusIds = this.getRoleMenuIds(roleIds);
        List<String> menuLinks = new ArrayList<>();
        for (Integer menuId : menusIds) {
            MenuModel menu = menuService.getById(menuId);
            if (menu != null) {
                menuLinks.add(menu.getLink());
            }
        }
        return menuLinks;
    }

    @Override
    public List<MenuTree> getRoleMenuTree(List<Integer> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return null;
        }
        QueryWrapper<RoleMenuModel> wrapper = new QueryWrapper<>();
        wrapper.in(RoleMenuModel.ROLE_ID, roleIds);
        List<Integer> roleMenus = super.baseMapper.selectList(wrapper)
                .stream()
                .map(RoleMenuModel::getMenuId)
                .collect(Collectors.toList());
        if (roleMenus.size() > 0) {
            return menuService.getTreeByMenuIds(roleMenus);
        }
        return null;
    }

    @Override
    public List<String> getRoleMenus(List<Integer> roleIds) {
        Assert.notEmpty(roleIds, "角色id集合不能为空");
        List<Integer> menuIds = this.getRoleMenuIds(roleIds);
        if (menuIds.size() > 0) {
            List<MenuModel> menuList = menuService.getBaseMapper().selectBatchIds(menuIds);
            if (menuList.size() > 0) {
                return menuList.stream()
                        .filter(o -> SysEnum.STATUS_NORMAL.getCode() == o.getStatus() && !StringUtils.equals(o.getLink(), "/"))
                        .map(MenuModel::getLink)
                        .collect(Collectors.toList());
            }
        }
        return null;
    }
}

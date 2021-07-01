package com.yinchd.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yinchd.web.dto.base.RestResult;
import com.yinchd.web.dto.request.MenuAddParam;
import com.yinchd.web.dto.response.MenuTree;
import com.yinchd.web.model.MenuModel;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author yinchd
 * @since 2021-03-17
 */
public interface MenuService extends IService<MenuModel> {

    /**
     * 菜单新增
     * @param param 新增参数
     */
    RestResult<String> save(MenuAddParam param);

    /**
     * 修改菜单状态
     * @param menuId 菜单id
     * @param status 状态
     */
    RestResult<String> status(int menuId, int status);

    /**
     * 删除菜单（物理删除）
     * @param menuIds 菜单id集合
     */
    RestResult<String> del(List<Integer> menuIds);

    /**
     * 获取菜单树（所有）
     */
    RestResult<List<MenuTree>> tree();

    /**
     * 根据菜单id组装菜单树
     * @param menuIds 菜单id集合
     */
    List<MenuTree> getTreeByMenuIds(List<Integer> menuIds);
}

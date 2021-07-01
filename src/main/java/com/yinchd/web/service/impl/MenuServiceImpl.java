package com.yinchd.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yinchd.web.config.UserContext;
import com.yinchd.web.constants.SysConst;
import com.yinchd.web.constants.SysEnum;
import com.yinchd.web.dto.base.RestResult;
import com.yinchd.web.dto.request.MenuAddParam;
import com.yinchd.web.dto.response.MenuTree;
import com.yinchd.web.mapper.MenuMapper;
import com.yinchd.web.model.MenuModel;
import com.yinchd.web.model.RoleMenuModel;
import com.yinchd.web.service.MenuService;
import com.yinchd.web.service.RoleMenuService;
import com.yinchd.web.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * 菜单管理 服务实现类
 * </p>
 *
 * @author yinchd
 * @since 2021-03-17
 */
@Service
@Slf4j
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuModel> implements MenuService {

    @Resource
    private RoleMenuService roleMenuService;

    @Override
    public RestResult<List<MenuTree>> tree() {
        return RestResult.ok(this.getTreeByMenuIds(null));
    }

    @Override
    public List<MenuTree> getTreeByMenuIds(List<Integer> menuIds) {
        QueryWrapper<MenuModel> qr = new QueryWrapper<>();
        if (menuIds != null) {
            qr.in(MenuModel.ID, menuIds);
        }
        // 数据进行排序查询
        qr.orderBy(true, true, MenuModel.LEVEL, MenuModel.RANK);
        List<MenuModel> menuList = super.baseMapper.selectList(qr);
        // 存放上级菜单
        List<MenuTree> topMenus = new ArrayList<>();
        // 存放所以被转换成页面展示对象的菜单
        List<MenuTree> menus = new ArrayList<>();
        for (MenuModel menu : menuList) {
            MenuTree tree = new MenuTree();
            BeanUtils.copyProperties(menu, tree);
            tree.setType(SysEnum.getMenuType(menu.getType()));
            tree.setStatus(SysEnum.getStatus(menu.getStatus()));
            tree.setCreateTime(DateUtils.format(menu.getCreateTime(), DateUtils.DEFAULT_PATTERN_YMD));
            if (menu.getLevel() == SysConst.TOP_LEVEL) {
                topMenus.add(tree);
            }
            menus.add(tree);
        }
        this.findChild(topMenus, menus);
        return menus;
    }

    @Override
    public RestResult<String> save(MenuAddParam param) {
        log.info("菜单{}，参数为：{}", param.getId() == null ? "新增" : "修改", param);
        MenuModel vo = new MenuModel();
        BeanUtils.copyProperties(param, vo);
        if (param.getPid() == null || param.getPid() == 0) {
            param.setLevel(SysConst.TOP_LEVEL);
        } else {
            if (super.baseMapper.selectById(param.getPid()) == null) {
                log.info("父级菜单：{}查询不存在。。", param.getPid());
                return RestResult.error("父级菜单不存在");
            }
        }
        int result;
        if (param.getId() == null || param.getId() == 0) {
            vo.setCreateBy(UserContext.get().getAccount());
            vo.setLastUpdateBy(vo.getCreateBy());
            result = super.baseMapper.insert(vo);
        } else {
            MenuModel menu = super.baseMapper.selectById(param.getId());
            if (menu == null) {
                return RestResult.error("菜单不存在");
            }
            vo.setId(param.getId());
            vo.setLastUpdateBy(vo.getCreateBy());
            result = super.baseMapper.updateById(vo);
        }
        log.info("菜单信息操作：{}", result > 0 ? "成功" : "失败");
        return result > 0 ? RestResult.ok("操作成功") : RestResult.error("操作失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResult<String> status(int menuId, int status) {
        MenuModel menu = super.baseMapper.selectById(menuId);
        if (menu == null) {
            return RestResult.error("菜单不存在");
        }
        if (!Arrays.asList(SysEnum.STATUS_FREEZE.getCode(), SysEnum.STATUS_NORMAL.getCode()).contains(status)) {
            return RestResult.error("状态值有误");
        }
        if (status == menu.getStatus()) {
            return RestResult.error("菜单状态已是最新");
        }
        MenuModel obj = new MenuModel();
        obj.setId(menuId);
        obj.setStatus(status);
        obj.setLastUpdateBy(UserContext.get().getAccount());
        int result = this.baseMapper.updateById(obj);
        String msg = "修改菜单状态" + (result > 0 ? "成功" : "失败");
        log.info(msg);
        return RestResult.ok(msg);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResult<String> del(List<Integer> menuIds) {
        log.info("删除菜单信息，menuIds：[{}]...", menuIds);
        // 从安全的角度来说这里应该是做级联删除，如删除了父菜单，则将父菜单下的子菜单级联删除，这里默认是由前端将父、子id都一起传过来
        int delete = super.baseMapper.deleteBatchIds(menuIds);
        if (delete > 0) {
            log.info("批量删除菜单记录：[{}]成功", menuIds);
            // 删除角色、部门与此菜单的映射关系
            QueryWrapper<RoleMenuModel> wrapper = new QueryWrapper<>();
            wrapper.in(RoleMenuModel.MENU_ID, menuIds);
            delete = roleMenuService.getBaseMapper().delete(wrapper);
            if (delete > 0) {
                log.info("角色菜单映射关系表中此菜单集合：{}：的映射清除：{}条成功...", menuIds, delete);
            }
        }
        return RestResult.ok();
    }

    /**
     * 获取下级菜单
     * @param topMenus 上级菜单
     * @param menus 所有菜单
     */
    private void findChild(List<MenuTree> topMenus, List<MenuTree> menus) {
        topMenus.forEach(parent -> {
            List<MenuTree> childs = new ArrayList<>();
            Iterator<MenuTree> it = menus.iterator();
            while (it.hasNext()) {
                MenuTree menu = it.next();
                if (menu.getPid().equals(parent.getId())) {
                    childs.add(menu);
                    it.remove();
                }
            }
            parent.setChild(childs);
            this.findChild(childs, menus);
        });
    }

}

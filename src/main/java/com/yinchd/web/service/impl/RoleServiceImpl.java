package com.yinchd.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yinchd.web.config.UserContext;
import com.yinchd.web.constants.SysEnum;
import com.yinchd.web.dto.base.RestResult;
import com.yinchd.web.dto.page.PageRequest;
import com.yinchd.web.dto.request.RoleAddParam;
import com.yinchd.web.dto.request.RoleQueryParam;
import com.yinchd.web.dto.response.RolePage;
import com.yinchd.web.mapper.RoleMapper;
import com.yinchd.web.model.RoleModel;
import com.yinchd.web.service.RoleMenuService;
import com.yinchd.web.service.RoleService;
import com.yinchd.web.service.UserRoleService;
import com.yinchd.web.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色管理 服务实现类
 * </p>
 *
 * @author yinchd
 * @since 2021-03-18
 */
@Service
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleModel> implements RoleService {

    @Resource
    private RoleMenuService roleMenuService;
    @Resource
    private UserRoleService userRoleService;

    @Override
    public IPage<RolePage> getPage(PageRequest<RoleQueryParam> request) {
        Page<RoleModel> page = new Page<>(request.getPageNum(), request.getPageSize());
        QueryWrapper<RoleModel> qr = new QueryWrapper<>();
        this.setRoleQueryParam(request, qr);
        IPage<RoleModel> result = this.baseMapper.selectPage(page, qr);
        List<RolePage> transList = result.getRecords().stream().map(role -> {
            // 将数据库实体转义成页面实体
            RolePage pageVO = new RolePage();
            BeanUtils.copyProperties(role, pageVO);
            if (role.getCreateTime() != null) {
                pageVO.setCreateTime(DateUtils.format(role.getCreateTime(), DateUtils.DEFAULT_PATTERN_YMD));
            }
            pageVO.setStatus(SysEnum.getStatus(role.getStatus()));
            // 查询角色菜单信息
            pageVO.setRoleMenus(roleMenuService.getRoleMenuIds(Collections.singletonList(role.getId())));
            return pageVO;
        }).collect(Collectors.toList());
        IPage<RolePage> resultPage = new Page<>();
        BeanUtils.copyProperties(result, resultPage);
        resultPage.setRecords(transList);
        return resultPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResult<String> save(RoleAddParam param) {
        boolean add = (param.getId() == null || param.getId() == 0);
        log.info("角色{}，参数为：{}...", add ? "新增" : "修改", param);
        RoleModel vo = new RoleModel();
        int result;
        if (add) {
            if (this.isRepeat(param.getName())) {
                return RestResult.error("角色名称有重复");
            }
            vo.setName(param.getName());
            vo.setNote(param.getNote());
            vo.setCreateBy(UserContext.get().getAccount());
            vo.setLastUpdateBy(vo.getCreateBy());
            result = super.baseMapper.insert(vo);
        } else {
            RoleModel role = super.baseMapper.selectById(param.getId());
            if (role == null) {
                return RestResult.error("角色不存在");
            }
            if (StringUtils.isNotBlank(param.getName()) && !StringUtils.equals(param.getName(), role.getName())) {
                if (this.isRepeat(param.getName())) {
                    return RestResult.error("角色名称有重复");
                }
                vo.setName(param.getName());
            }
            if (StringUtils.isNotBlank(param.getNote())) {
                vo.setNote(param.getNote());
            }
            vo.setId(param.getId());
            vo.setLastUpdateBy(UserContext.get().getAccount());
            vo.setLastUpdateTime(LocalDateTime.now());
            result = super.baseMapper.updateById(vo);
        }
        boolean isOk = roleMenuService.saveRoleMenus(vo.getId(), param.getMenus());
        log.info("保存角色资源信息操作：{}", isOk ? "成功" : "失败");
        return result > 0 ? RestResult.ok() : RestResult.error("操作失败");
    }

    /**
     * 检测角色名称是否重复
     * @param name 角色标识或名称
     * @return 重复返回true, 反之false
     */
    public boolean isRepeat(String name) {
        QueryWrapper<RoleModel> checkQr = new QueryWrapper<>();
        checkQr.eq(RoleModel.NAME, name);
        return super.baseMapper.selectCount(checkQr) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResult<String> status(int roleId, int status) {
        RoleModel role = super.baseMapper.selectById(roleId);
        if (role == null) {
            return RestResult.error("角色不存在");
        }
        if (!Arrays.asList(SysEnum.STATUS_FREEZE.getCode(), SysEnum.STATUS_NORMAL.getCode()).contains(status)) {
            return RestResult.error("状态值有误");
        }
        if (status == role.getStatus()) {
            return RestResult.error("角色状态已是最新");
        }
        RoleModel obj = new RoleModel();
        obj.setId(roleId);
        obj.setStatus(status);
        obj.setLastUpdateBy(UserContext.get().getAccount());
        int result = this.baseMapper.updateById(obj);
        return RestResult.ok("修改角色状态" + (result > 0 ? "成功" : "失败"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResult<String> del(int roleId) {
        log.info("删除角色信息，id：{}...", roleId);
        RoleModel role = super.baseMapper.selectById(roleId);
        if (role == null) {
            return RestResult.error("角色查询不存在");
        }
        int delete = super.baseMapper.deleteById(roleId);
        if (delete > 0) {
            log.info("删除角色，id：[{}]成功", roleId);
            // 删除用户与此角色的映射关系
            delete = userRoleService.deleteByRoleId(roleId);
            log.info("删除用户与角色的映射关系成功，删除了{}条", delete);
        }
        return delete > 0 ? RestResult.ok("删除成功") : RestResult.error("删除失败");
    }

    @Override
    public List<Integer> checkRoleIds(List<Integer> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return null;
        }
        return super.baseMapper.selectBatchIds(roleIds)
                .stream()
                .filter(o -> o.getStatus() == SysEnum.STATUS_NORMAL.getCode())
                .map(RoleModel::getId)
                .collect(Collectors.toList());
    }

    private void setRoleQueryParam(PageRequest<RoleQueryParam> request, QueryWrapper<RoleModel> qr) {
        RoleQueryParam param = request.getParam();
        if (StringUtils.isNotBlank(param.getName())) {
            qr.like(RoleModel.NAME, param.getName());
        }
        // 查询拥有菜单（param.getMenus()）的角色
        if (CollectionUtils.isNotEmpty(param.getMenus())) {
            List<Integer> roles = roleMenuService.getRoleIdsByMenuIds(param.getMenus());
            if (CollectionUtils.isEmpty(roles)) {
                // 如果没有任何角色关联这个菜单，则手动让查不出数据
                qr.lt(RoleModel.ID, 0);
            } else {
                qr.in(RoleModel.ID, roles);
            }
        }
        String orderBy = StringUtils.isBlank(request.getOrderBy()) ? RoleModel.CREATE_TIME : request.getOrderBy();
        boolean isAsc = StringUtils.isBlank(request.getOrder()) || StringUtils.equals(request.getOrder(), "asc");
        qr.orderBy(true, isAsc, orderBy);
    }
}

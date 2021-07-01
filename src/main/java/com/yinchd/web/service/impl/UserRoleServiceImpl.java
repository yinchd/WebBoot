package com.yinchd.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yinchd.web.mapper.UserRoleMapper;
import com.yinchd.web.model.UserRoleModel;
import com.yinchd.web.service.CacheService;
import com.yinchd.web.service.RoleService;
import com.yinchd.web.service.UserRoleService;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户角色映射表 服务实现类
 * </p>
 *
 * @author yinchd
 * @since 2021-03-18
 */
@Service
@Slf4j
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleModel> implements UserRoleService {

    @Resource
    private RoleService roleService;
    @Resource
    private CacheService cacheService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUserRoles(Integer userId, List<Integer> roleIds, boolean isEdit) {
        Assert.notNull(userId, "用户id不能为空");
        log.info("保存用户：[{}] 角色信息：[{}]", userId, roleIds);
        if (isEdit) {
            log.info("删除用户与角色的映射关系...");
            QueryWrapper<UserRoleModel> wrapper = new QueryWrapper<>();
            wrapper.in(UserRoleModel.USER_ID, userId);
            int delete = super.baseMapper.delete(wrapper);
            if (delete > 0) {
                log.info("对用户{}之前关联的角色清除成功", userId);
            }
            // 清空用户与角色的映射缓存
            cacheService.invalidUserMenuCache();
        }
        if (CollectionUtils.isEmpty(roleIds)) {
            return true;
        }
        long result = roleService.getBaseMapper()
                .selectBatchIds(roleIds)
                .stream()
                .map(o -> {
                    UserRoleModel userRoleModel = new UserRoleModel();
                    userRoleModel.setUserId(userId);
                    userRoleModel.setRoleId(o.getId());
                    return super.baseMapper.insert(userRoleModel);
                }).count();
        return result > 0;
    }

    @Override
    public List<Integer> getRoleIdsByUserId(int userId) {
        QueryWrapper<UserRoleModel> qr = new QueryWrapper<>();
        qr.eq(UserRoleModel.USER_ID, userId);
        return super.baseMapper.selectList(qr)
                .stream()
                .map(UserRoleModel::getRoleId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByRoleId(int roleId) {
        log.info("删除用户角色映射表中角色id为：[{}]的记录", roleId);
        QueryWrapper<UserRoleModel> wrapper = new QueryWrapper<>();
        wrapper.eq(UserRoleModel.ROLE_ID, roleId);
        return super.baseMapper.delete(wrapper);
    }
}

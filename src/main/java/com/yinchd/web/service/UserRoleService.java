package com.yinchd.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yinchd.web.model.UserRoleModel;

import java.util.List;

/**
 * <p>
 * 用户角色映射表 服务类
 * </p>
 *
 * @author yinchd
 * @since 2021-03-18
 */
public interface UserRoleService extends IService<UserRoleModel> {

    /**
     * 保存用户角色信息
     * @param userId 用户id
     * @param roleIds 角色集合
     * @param isEdit 是否是修改，如果是新增时，不用清除用户与角色的映射关系，如果是修改，则要先清除之前的映射关系，再插入新的映射
     */
    boolean saveUserRoles(Integer userId, List<Integer> roleIds, boolean isEdit);

    /**
     * 查询用关联的所有角色信息
     * @param userId 用户id
     */
    List<Integer> getRoleIdsByUserId(int userId);

    /**
     * 删除用户角色映射表中此角色的映射关系
     * @param roleId 角色id
     */
    int deleteByRoleId(int roleId);
}

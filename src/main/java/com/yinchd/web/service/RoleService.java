package com.yinchd.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yinchd.web.dto.base.RestResult;
import com.yinchd.web.dto.page.PageRequest;
import com.yinchd.web.dto.request.RoleAddParam;
import com.yinchd.web.dto.request.RoleQueryParam;
import com.yinchd.web.dto.response.RolePage;
import com.yinchd.web.model.RoleModel;

import java.util.List;

/**
 * <p>
 * 角色管理 服务类
 * </p>
 *
 * @author yinchd
 * @since 2021-03-18
 */
public interface RoleService extends IService<RoleModel> {

    /**
     * 检查角色是否存在，并返回其中有效的角色id
     * @param roleIds 角色id集合
     */
    List<Integer> checkRoleIds(List<Integer> roleIds);

    /**
     * 角色列表
     * @param request 查询参数
     */
    IPage<RolePage> getPage(PageRequest<RoleQueryParam> request);

    /**
     * 角色新增
     * @param param 新增参数
     */
    RestResult<String> save(RoleAddParam param);

    /**
     * 修改状态
     * @param roleId 角色id
     * @param status 状态
     */
    RestResult<String> status(int roleId, int status);

    /**
     * 删除角色（物理删除）
     * @param roleId 角色id
     */
    RestResult<String> del(int roleId);

}

package com.yinchd.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yinchd.web.dto.base.RestResult;
import com.yinchd.web.dto.page.PageRequest;
import com.yinchd.web.dto.request.UserAddParam;
import com.yinchd.web.dto.request.UserQueryParam;
import com.yinchd.web.dto.response.MenuTree;
import com.yinchd.web.dto.response.UserInfo;
import com.yinchd.web.dto.response.UserPage;
import com.yinchd.web.model.UserModel;

import java.util.List;

/**
 * <p>
 * 用户管理 服务类
 * </p>
 *
 * @author yinchd
 * @since 2021-03-10
 */
public interface UserService extends IService<UserModel> {

    /**
     * 根据用户名查询
     * @param account 用户名
     */
    UserModel findByAccount(String account);

    /**
     * 用户登录
     * @param user 登录用户
     */
    UserInfo login(UserModel user);

    /**
     * 刷新token
     * @param token 请求token
     * @param refreshToken 刷新token
     * @return 刷新过的token
     */
    RestResult<Object> refreshToken(String token, String refreshToken);

    /**
     * 用户列表
     * @param request 查询参数
     */
    IPage<UserPage> getPage(PageRequest<UserQueryParam> request);

    /**
     * 用户新增
     * @param param 新增参数
     */
    RestResult<String> save(UserAddParam param);

    /**
     * 修改用户状态
     * @param userId 用户id
     * @param status 用户状态
     */
    RestResult<String> status(int userId, int status);

    /**
     * 删除用户（逻辑删除）
     * @param userId 用户id
     */
    RestResult<String> del(int userId);

    /**
     * 查询用户具有的菜单资源树
     * @param userId 用户id
     * @return 该用户的菜单资源树
     */
    RestResult<List<MenuTree>> menus(int userId);

    /**
     * 获取用户菜单菜单集合
     * @param userId 用户id
     */
    List<String> getUserMenus(Integer userId);
}

package com.yinchd.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yinchd.web.config.JwtProperties;
import com.yinchd.web.config.UserContext;
import com.yinchd.web.constants.JwtConstants;
import com.yinchd.web.constants.RedisConstants;
import com.yinchd.web.constants.SysConst;
import com.yinchd.web.constants.SysEnum;
import com.yinchd.web.dto.base.RestResult;
import com.yinchd.web.dto.page.PageRequest;
import com.yinchd.web.dto.request.UserAddParam;
import com.yinchd.web.dto.request.UserQueryParam;
import com.yinchd.web.dto.response.MenuTree;
import com.yinchd.web.dto.response.UserInfo;
import com.yinchd.web.dto.response.UserPage;
import com.yinchd.web.mapper.UserMapper;
import com.yinchd.web.model.UserModel;
import com.yinchd.web.service.RoleMenuService;
import com.yinchd.web.service.RoleService;
import com.yinchd.web.service.UserRoleService;
import com.yinchd.web.service.UserService;
import com.yinchd.web.utils.BCryptEncoder;
import com.yinchd.web.utils.DateUtils;
import com.yinchd.web.utils.JwtUtils;
import com.yinchd.web.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户管理 服务实现类
 * </p>
 *
 * @author yinchd
 * @since 2021-03-10
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserModel> implements UserService {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private JwtProperties jwtProperties;
    @Resource
    private RoleService roleService;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private RoleMenuService roleMenuService;

    @Override
    public UserModel findByAccount(String account) {
        QueryWrapper<UserModel> qr = new QueryWrapper<>();
        qr.eq(UserModel.ACCOUNT, account);
        qr.eq(UserModel.DEL_FLAG, SysEnum.DEL_FLAG_VALID.getCode());
        List<UserModel> userModels = super.baseMapper.selectList(qr);
        return userModels.size() == 1 ? userModels.get(0) : null;
    }

    @Override
    public UserInfo login(UserModel user) {
        // token
        String token = JwtUtils.generateToken(user, jwtProperties.getPrivateKey(), jwtProperties.getAccessExpire());
        // 刷新token
        String refreshToken = JwtUtils.generateToken(user, jwtProperties.getPrivateKey(), jwtProperties.getRefreshExpire());
        return new UserInfo(user.getId(), user.getAccount(), token, refreshToken);
    }

    @Override
    public RestResult<Object> refreshToken(String token, String refreshToken) {
        // 先判断刷新token凭证是否有效
        if (JwtUtils.isTokenExpired(refreshToken, this.jwtProperties.getPublicKey())) {
            log.error("refreshToken已过期...");
            return RestResult.error(HttpStatus.UNAUTHORIZED.value(), "刷新token已过期，请重新登录");
        }
        Integer userId = null;
        String account = null;
        try {
            // 从token中解析出用户信息
            UserInfo userInfo = JwtUtils.getUserFromToken(token, this.jwtProperties.getPublicKey());
            userId = userInfo.getId();
            account = userInfo.getAccount();
        } catch (Exception e) {
            log.error("解析token异常，", e);
            if (e instanceof ExpiredJwtException) {
                ExpiredJwtException exception = (ExpiredJwtException) e;
                Claims claims = exception.getClaims();
                if (claims != null) {
                    userId = Integer.valueOf(claims.get(JwtConstants.JWT_KEY_USER_ID).toString());
                    account = claims.get(JwtConstants.JWT_KEY_USER_ACCOUNT).toString();
                }
            }
        }
        if (userId != null && account != null) {
            QueryWrapper<UserModel> wrapper = new QueryWrapper<>();
            wrapper.eq(UserModel.ID, userId);
            wrapper.eq(UserModel.ACCOUNT, account);
            UserModel user = super.baseMapper.selectOne(wrapper);
            if (user != null) {
                if (SysEnum.DEL_FLAG_INVALID.getCode() == user.getDelFlag()) {
                    log.info("刷新token失败，用户已被删除：{}", account);
                    return RestResult.error(HttpStatus.UNAUTHORIZED.value(), "用户已被移除，请联系管理员");
                }
                if (SysEnum.STATUS_FREEZE.getCode() == user.getStatus()) {
                    log.info("刷新token失败，用户已被禁用：{}", account);
                    return RestResult.error(HttpStatus.UNAUTHORIZED.value(), "用户已禁用，请联系管理员");
                }
                Object newToken = JwtUtils.generateToken(user, jwtProperties.getPrivateKey(), jwtProperties.getAccessExpire());
                // 防止前端并发请求，这里将新token缓存3分钟
                redisUtil.set(RedisConstants.TOKEN_REFRESH + token, newToken.toString(), 3, TimeUnit.SECONDS);
                return RestResult.ok(newToken);
            }
        }
        return RestResult.error(HttpStatus.UNAUTHORIZED.value(), "获取新token失败，请重新登录");
    }

    @Override
    public IPage<UserPage> getPage(PageRequest<UserQueryParam> request) {
        Page<UserModel> page = new Page<>(request.getPageNum(), request.getPageSize());
        QueryWrapper<UserModel> qr = new QueryWrapper<>();
        this.setUserQueryParam(request, qr);
        IPage<UserModel> result = this.baseMapper.selectPage(page, qr);
        List<UserPage> transList = result.getRecords().stream().map(o -> {
            // 将数据库实体转义成页面实体
            UserPage pageVO = new UserPage();
            BeanUtils.copyProperties(o, pageVO);
            if (o.getSex() != null) {
                pageVO.setSex(SysEnum.getSex(o.getSex()));
            }
            if (o.getStatus() != null) {
                pageVO.setStatus(SysEnum.getStatus(o.getStatus()));
            }
            if (o.getCreateTime() != null) {
                pageVO.setCreateTime(DateUtils.format(o.getCreateTime(), DateUtils.DEFAULT_PATTERN_YMD));
            }
            return pageVO;
        }).collect(Collectors.toList());
        IPage<UserPage> resultPage = new Page<>();
        BeanUtils.copyProperties(result, resultPage);
        resultPage.setRecords(transList);
        return resultPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResult<String> save(UserAddParam param) {
        Integer userId = param.getId();
        boolean add = userId == null || userId == 0;
        log.info("用户{}，参数为：{}", add ? "新增" : "修改", param);
        UserModel obj = new UserModel();
        BeanUtils.copyProperties(param, obj);
        if (add) {
            // 新增，校验用户是否重复
            if (this.isAccountRepeat(param.getAccount())) {
                log.info("新增用户时用户名重复：{}", param.getAccount());
                return RestResult.error("用户名已被使用，请检查");
            }
            obj.setCreateBy(UserContext.get().getAccount());
            obj.setLastUpdateBy(obj.getCreateBy());
            // 处理密码，如果没传密码，则给上默认密码
            if (StringUtils.isBlank(param.getPassword())) {
                param.setPassword(SysConst.DEFAULT_PASSWORD);
            }
        } else {
            UserModel user = this.getUserById(userId);
            if (user == null) {
                return RestResult.error("用户不存在");
            }
            obj.setId(userId);
            // 修改时不允许改用户名
            obj.setAccount(null);
            obj.setLastUpdateBy(UserContext.get().getAccount());
        }
        if (param.getPassword() != null) {
            obj.setPassword(BCryptEncoder.encode(param.getPassword()));
        }
        if (CollectionUtils.isNotEmpty(param.getRoles())) {
            List<Integer> existIds = roleService.checkRoleIds(param.getRoles());
            if (CollectionUtils.isEmpty(existIds)) {
                return RestResult.error("请检查用户所属角色是否存在或者是否有效");
            }
            param.setRoles(existIds);
        }
        int result;
        if (add) {
            result = super.baseMapper.insert(obj);
        } else {
            result = super.baseMapper.updateById(obj);
        }
        // 是否是编辑
        boolean isEdit = !add;
        // 保存用户权限
        boolean save = userRoleService.saveUserRoles(obj.getId(), param.getRoles(), isEdit);
        log.info("保存用户角色信息：{}", save ? "成功" : "失败");
        return RestResult.ok((add ? "新增" : "修改") + "用户" + (result > 0 ? "成功" : "失败"));
    }

    @Override
    public RestResult<String> status(int userId, int status) {
        log.info("修改用户状态，userId:[{}], status:[{}]", userId, status);
        UserModel user = this.getUserById(userId);
        if (user == null) {
            return RestResult.error("用户不存在");
        }
        if (!Arrays.asList(SysEnum.STATUS_FREEZE.getCode(), SysEnum.STATUS_NORMAL.getCode()).contains(status)) {
            return RestResult.error("状态值有误");
        }
        if (status == user.getStatus()) {
            return RestResult.error("状态值已为最新");
        }
        UserModel obj = new UserModel();
        obj.setId(userId);
        obj.setStatus(status);
        obj.setLastUpdateBy(UserContext.get().getAccount());
        int result = this.baseMapper.updateById(obj);
        return RestResult.ok("修改用户状态" + (result > 0 ? "成功" : "失败"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResult<String> del(int userId) {
        log.info("逻辑删除用户，userId：{}...", userId);
        UserModel user = this.getUserById(userId);
        if (user == null) {
            return RestResult.error("用户不存在");
        }
        UserModel userUpd = new UserModel();
        userUpd.setId(userId);
        userUpd.setDelFlag(SysEnum.DEL_FLAG_INVALID.getCode());
        userUpd.setLastUpdateBy(UserContext.get().getAccount());
        int result = this.baseMapper.updateById(userUpd);
        String msg = "删除用户" + (result > 0 ? "成功" : "失败");
        log.info(msg);
        return RestResult.ok(msg);
    }

    @Override
    public RestResult<List<MenuTree>> menus(int userId) {
        UserModel user = this.getUserById(userId);
        if (user == null) {
            return RestResult.error("用户不存在");
        }
        if (SysEnum.STATUS_FREEZE.getCode() == user.getStatus()) {
            return RestResult.error("用户已被禁用");
        }
        List<Integer> allRoles = new ArrayList<>();
        // 查询用户所有的角色信息
        List<Integer> roleIds = userRoleService.getRoleIdsByUserId(userId);
        if (CollectionUtils.isNotEmpty(roleIds)) {
            allRoles.addAll(roleIds);
        }
        List<MenuTree> trees = new ArrayList<>();
        if (allRoles.size() > 0) {
            trees = roleMenuService.getRoleMenuTree(allRoles);
        }
        return RestResult.ok(trees);
    }

    @Override
    public List<String> getUserMenus(Integer userId) {
        UserModel user = super.getById(userId);
        // 用户不存在或者用户禁用直接返回null
        if (user == null || SysEnum.DEL_FLAG_INVALID.getCode() == user.getDelFlag() || SysEnum.STATUS_FREEZE.getCode() == user.getStatus()) {
            return null;
        }
        List<Integer> userAllRoles = new ArrayList<>();
        // 获取用户的角色菜单
        List<Integer> roleIds = userRoleService.getRoleIdsByUserId(userId);
        if (CollectionUtils.isNotEmpty(roleIds)) {
            userAllRoles.addAll(roleIds);
        }
        if (userAllRoles.size() > 0) {
            return roleMenuService.getRoleMenus(userAllRoles);
        }
        return null;
    }

    /**
     * 根据用户id查询，如果查询结果为空或者id为管理员id，返回null
     * @param userId 用户id
     */
    private UserModel getUserById(Integer userId) {
        if (userId == SysConst.ADMIN_ID) {
            log.info("修改的用户为超级管理员，被拦截..");
            return null;
        }
        UserModel user = super.baseMapper.selectById(userId);
        if (user == null || SysEnum.DEL_FLAG_INVALID.getCode() == user.getDelFlag()) {
            return null;
        }
        return user;
    }

    /**
     * 校验用户账号是否重复
     * @param account 用户账户
     */
    private boolean isAccountRepeat(String account) {
        QueryWrapper<UserModel> check = new QueryWrapper<>();
        check.eq(UserModel.ACCOUNT, account);
        return super.baseMapper.selectCount(check) > 0;
    }

    private void setUserQueryParam(PageRequest<UserQueryParam> request, QueryWrapper<UserModel> qr) {
        UserQueryParam param = request.getParam();
        if (StringUtils.isNotBlank(param.getAccount())) {
            qr.like(UserModel.ACCOUNT, param.getAccount());
        }
        if (StringUtils.isNotBlank(param.getMobile())) {
            qr.eq(UserModel.MOBILE, param.getMobile());
        }
        if (Arrays.asList(1, 2).contains(param.getSex())) {
            qr.eq(UserModel.SEX, param.getSex());
        }
        if (Arrays.asList(0, 1).contains(param.getStatus())) {
            qr.eq(UserModel.STATUS, param.getStatus());
        }
        // 只查询未删除的账户
        qr.eq(UserModel.DEL_FLAG, SysEnum.DEL_FLAG_VALID.getCode());
        // 隐藏管理员账户（默认管理员id为1）
        qr.ne(UserModel.ID, SysConst.ADMIN_ID);
        String orderBy = StringUtils.isBlank(request.getOrderBy()) ? UserModel.CREATE_TIME : request.getOrderBy();
        boolean isAsc = StringUtils.equals(request.getOrder(), "asc");
        qr.orderBy(true, isAsc, orderBy);
    }
}

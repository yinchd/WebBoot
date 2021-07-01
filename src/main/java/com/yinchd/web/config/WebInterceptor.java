package com.yinchd.web.config;

import com.yinchd.web.annatation.OpenApi;
import com.yinchd.web.constants.SysConst;
import com.yinchd.web.constants.SysEnum;
import com.yinchd.web.dto.base.RestResult;
import com.yinchd.web.dto.response.UserInfo;
import com.yinchd.web.model.UserModel;
import com.yinchd.web.service.CacheService;
import com.yinchd.web.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 全局拦截器
 * @author yinchd
 * @date 2019/11/6
 */
@Slf4j
@Component
public class WebInterceptor implements HandlerInterceptor {

    @Resource
    private JwtProperties jwtProperties;
    @Resource
    private CacheService cacheService;

    /**
     * 对除白名单及用@OpenApi注解修饰的接口进行拦截，解析token，判断token中包含的用户信息是否合法以及是否有访问对应资源的权限
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String requestURI = request.getRequestURI();
        // 静态资源及白名单直接放行
        if ((handler instanceof ResourceHttpRequestHandler) || isSwaggerOrStatics(requestURI)) {
            return true;
        }
        // @OpenApi修饰的方法或者类直接放行
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (handlerMethod.getMethod().isAnnotationPresent(OpenApi.class) ||
                handlerMethod.getBeanType().isAnnotationPresent(OpenApi.class)) {
            return true;
        }
        String token = request.getHeader(this.jwtProperties.getName());
        if (StringUtils.isEmpty(token)) {
            this.unauthorized(response);
            return false;
        }
        try {
            // 从token中解析出用户信息
            UserInfo userInfo = JwtUtils.getUserFromToken(token, this.jwtProperties.getPublicKey());
            // 从缓存服务中获取用户信息
            UserModel user = cacheService.getUserCache(userInfo.getId());
            // 如果是非管理员用户，在这里拦截菜单权限
            if (userInfo.getId() != SysConst.ADMIN_ID) {
                // 从缓存服务中获取用户菜单信息
                List<String> userMenus = cacheService.getUserMenuCache(userInfo.getId());
                if (userMenus != null && !userMenus.contains(requestURI)) {
                    log.info("用户角色对应的菜单链接集合中不包含请求的路径：{}", requestURI);
                    this.accessDenied(response);
                    return false;
                }
            }
            if (user == null) {
                this.unauthorized(response);
                return false;
            }
            if (SysEnum.DEL_FLAG_INVALID.getCode() == user.getDelFlag()) {
                this.userInvalid(response);
                return false;
            }
            if (SysEnum.STATUS_FREEZE.getCode() == user.getStatus()) {
                this.userFreeze(response);
                return false;
            }
            UserContext.set(user);
            return true;
        } catch (Exception e) {
            log.error("preHandle拦截到异常,", e);
            if (e instanceof ExpiredJwtException) {
                this.needRefresh(response);
            } else if (e instanceof SignatureException) {
                this.tokenInvalid(response);
            }
            return false;
        }
    }

    private boolean isSwaggerOrStatics(String requestURI) {
        // 自定义免拦截的静态地址
        List<String> statics = Arrays.asList("/v3/api-docs", "/swagger", "/error", "/druid", "/doc.html");
        for (String url : statics) {
            if (requestURI.startsWith(url)) {
                return true;
            }
        }
        return false;
    }

    private void userInvalid(HttpServletResponse response) throws IOException {
        log.info("用户已被移除，请联系管理员...");
        this.reponse(response, HttpStatus.FORBIDDEN.value(), "您的账户已被移除，请联系管理员!");
    }

    private void tokenInvalid(HttpServletResponse response) throws IOException {
        log.info("请求token不合法...");
        this.reponse(response, HttpStatus.FORBIDDEN.value(), "请求token不合法!");
    }

    private void userFreeze(HttpServletResponse response) throws IOException {
        log.info("用户已被禁用...");
        this.reponse(response, HttpStatus.UNAUTHORIZED.value(), "您的账户已被禁用，请联系管理员!");
    }

    private void unauthorized(HttpServletResponse response) throws IOException {
        log.info("未授权访问，请重新登录...");
        this.reponse(response, HttpStatus.UNAUTHORIZED.value(), "请重新登录!");
    }

    private void accessDenied(HttpServletResponse response) throws IOException {
        log.info("没有访问此菜单的权限...");
        this.reponse(response, HttpStatus.UNAUTHORIZED.value(), "没有访问此资源的权限!");
    }

    private void needRefresh(HttpServletResponse response) throws IOException {
        log.info("需更新token...");
        this.reponse(response, HttpStatus.NOT_ACCEPTABLE.value(), "请刷新token");
    }

    private void reponse(HttpServletResponse response, int code, String msg) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(code);
        response.getWriter().write(RestResult.error(code, msg).toString());
    }
}

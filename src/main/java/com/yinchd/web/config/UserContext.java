package com.yinchd.web.config;

import com.yinchd.web.model.UserModel;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * 当前用户持有容器（内存泄漏安全类）
 * @author yinchd
 * @date 2019/9/20
 */
public class UserContext {
    /**
     * 保存本地线程信息
     */
    private static final String SECURITY_CONTEXT_ATTRIBUTES = "SECURITY_CONTEXT";

    public static void set(UserModel userInfo) {
        if (get() == null) {
            RequestContextHolder.currentRequestAttributes().setAttribute(SECURITY_CONTEXT_ATTRIBUTES, userInfo, RequestAttributes.SCOPE_REQUEST);
        }
    }

    public static UserModel get() {
        return (UserModel)RequestContextHolder.currentRequestAttributes().getAttribute(SECURITY_CONTEXT_ATTRIBUTES, RequestAttributes.SCOPE_REQUEST);
    }

}

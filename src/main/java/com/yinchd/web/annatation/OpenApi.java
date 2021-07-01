package com.yinchd.web.annatation;

import java.lang.annotation.*;

/**
 * @author yinchd
 * @version 1.0
 * @description 开放权限注解
 * @date 2019/3/15 14:23
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface OpenApi {

}

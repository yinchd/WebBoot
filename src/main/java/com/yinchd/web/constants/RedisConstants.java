package com.yinchd.web.constants;

/**
 * RedisKey常量类
 * @author yinchd
 * @date 2019/9/29
*/
public interface RedisConstants {

	String KAPTCHA_KEY = "kaptcha:";

	/**
	 * 用户信息缓存
	 */
	String TOKEN_REFRESH = "TOKEN:REFRESH:";

	String PAGE_VIEW_COUNT = "PV:";
}

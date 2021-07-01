package com.yinchd.web.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.yinchd.web.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
* CacheBuilder缓存服务
* @author yinchd
* @since 2019/3/18 11:34
* @version 2.6
**/
@Component
@Slf4j
public class CacheService {

    @Resource
    private UserService userService;

    /**
     * 用户缓存（userId -> UserModel），默认缓存10分钟
     */
    private final Cache<Integer, UserModel> USER_CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();
    /**
     * 用户菜单缓存（userId -> List<String>），默认缓存20分钟
     */
    private final LoadingCache<Integer, List<String>> USER_MENU_CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(20, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build(new CacheLoader<Integer, List<String>>() {
                @Override
                public List<String> load(Integer id) {
                    log.info("初始化用户菜单缓存，缓存用户id: {}", id);
                    return userService.getUserMenus(id);
                }
            });
    /**
     * 从缓存中获取用户信息，没有话从库中查询
     * @param userId 用户id
     */
    public UserModel getUserCache(Integer userId) throws ExecutionException {
        return USER_CACHE.get(userId, () -> {
            log.info("初始化用户缓存，缓存用户id: {}", userId);
            return userService.getById(userId);
        });
    }

    /**
     * 从缓存中获取用户菜单信息，没有话从库中查询
     * @param userId 用户id
     */
    public List<String> getUserMenuCache(Integer userId) throws ExecutionException {
        return USER_MENU_CACHE.get(userId);
    }

    /**
     * 清空用户与菜单的映射缓存
     */
    public void invalidUserMenuCache() {
        USER_MENU_CACHE.invalidateAll();
    }
}

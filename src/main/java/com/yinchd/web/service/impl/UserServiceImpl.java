package com.yinchd.web.service.impl;

import com.yinchd.web.model.UserModel;
import com.yinchd.web.mapper.UserMapper;
import com.yinchd.web.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户管理 服务实现类
 * </p>
 *
 * @author yinchd
 * @since 2021-04-25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserModel> implements UserService {

}

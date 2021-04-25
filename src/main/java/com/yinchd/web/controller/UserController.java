package com.yinchd.web.controller;


import com.yinchd.web.model.UserModel;
import com.yinchd.web.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 用户管理 前端控制器
 * </p>
 *
 * @author yinchd
 * @since 2021-04-25
 */
@RestController
@RequestMapping("user")
@Api(tags = "用户管理")
@Slf4j
public class UserController {

    @Resource
    UserService userService;

    @GetMapping("get")
    @ApiOperation("用户列表")
    public UserModel getUser() {
        log.info("查询用户列表");
        return userService.getById(1);
    }

}


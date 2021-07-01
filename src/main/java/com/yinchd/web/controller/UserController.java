package com.yinchd.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yinchd.web.dto.base.RestResult;
import com.yinchd.web.dto.page.PageRequest;
import com.yinchd.web.dto.request.UserAddParam;
import com.yinchd.web.dto.request.UserQueryParam;
import com.yinchd.web.dto.response.MenuTree;
import com.yinchd.web.dto.response.UserPage;
import com.yinchd.web.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author yinchd
 * @version 1.0
 * @description 用户中心
 */
@RestController
@RequestMapping("user")
@Api(tags = "用户管理")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("page")
    @ApiOperation("列表")
    public IPage<UserPage> page(@RequestBody @Valid PageRequest<UserQueryParam> request) {
        return userService.getPage(request);
    }

    @PostMapping("save")
    @ApiOperation("新增/修改")
    public RestResult<String> save(@RequestBody @Valid UserAddParam param) {
        return userService.save(param);
    }

    @GetMapping("status")
    @ApiOperation("启用/禁用")
    public RestResult<String> status(@RequestParam("userId") int userId, @RequestParam("status") int status) {
        return userService.status(userId, status);
    }

    @GetMapping("del")
    @ApiOperation("删除")
    public RestResult<String> del(@RequestParam("userId") int userId) {
        return userService.del(userId);
    }

    @GetMapping("menus")
    @ApiOperation("用户菜单树")
    public RestResult<List<MenuTree>> menus(@RequestParam("userId") int userId) {
        return userService.menus(userId);
    }

}

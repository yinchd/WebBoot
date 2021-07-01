package com.yinchd.web.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yinchd.web.dto.base.RestResult;
import com.yinchd.web.dto.page.PageRequest;
import com.yinchd.web.dto.request.RoleAddParam;
import com.yinchd.web.dto.request.RoleQueryParam;
import com.yinchd.web.dto.response.RolePage;
import com.yinchd.web.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author yinchd
 * @since 2021-03-18
 */
@RestController
@RequestMapping("role")
@Api(tags = "角色管理")
public class RoleController {

    @Resource
    private RoleService roleService;

    @PostMapping("page")
    @ApiOperation("列表")
    public IPage<RolePage> page(@RequestBody @Valid PageRequest<RoleQueryParam> request) {
        return roleService.getPage(request);
    }

    @PostMapping("save")
    @ApiOperation("新增/修改")
    public RestResult<String> save(@RequestBody @Valid RoleAddParam param) {
        return roleService.save(param);
    }

    @GetMapping("status")
    @ApiOperation("启用/禁用")
    public RestResult<String> status(@RequestParam("roleId") int roleId, @RequestParam("status") int status) {
        return roleService.status(roleId, status);
    }

    @GetMapping("del")
    @ApiOperation("删除")
    public RestResult<String> del(@RequestParam("roleId") int roleId) {
        return roleService.del(roleId);
    }

}


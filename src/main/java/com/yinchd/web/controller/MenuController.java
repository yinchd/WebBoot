package com.yinchd.web.controller;

import com.yinchd.web.dto.base.RestResult;
import com.yinchd.web.dto.request.MenuAddParam;
import com.yinchd.web.dto.response.MenuTree;
import com.yinchd.web.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author yinchd
 * @version 1.0
 * @description 菜单中心
 * @date 2021-03-17 17:15
 */
@RestController
@RequestMapping("menu")
@Api(tags = "菜单管理")
public class MenuController {

    @Resource
    private MenuService menuService;

    @GetMapping("tree")
    @ApiOperation("菜单树")
    public RestResult<List<MenuTree>> tree() {
        return menuService.tree();
    }

    @PostMapping("save")
    @ApiOperation("新增/修改")
    public RestResult<String> save(@RequestBody @Valid MenuAddParam param) {
        return menuService.save(param);
    }

    @GetMapping("status")
    @ApiOperation("启用/禁用")
    public RestResult<String> status(@RequestParam("menuId") int menuId, @RequestParam("status") int status) {
        return menuService.status(menuId, status);
    }

    @PostMapping("del")
    @ApiOperation("删除")
    public RestResult<String> del(@RequestBody List<Integer> menuIds) {
        return menuService.del(menuIds);
    }

}


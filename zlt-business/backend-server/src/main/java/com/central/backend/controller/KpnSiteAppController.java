package com.central.backend.controller;

import java.util.Map;

import com.central.backend.service.IKpnSiteAppService;
import com.central.common.annotation.LoginUser;
import com.central.common.model.KpnSiteApp;
import com.central.common.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import com.central.common.model.PageResult;
import com.central.common.model.Result;

/**
 * 站点app更新配置
 *
 * @author yixiu
 * @date 2023-02-21 19:46:07
 */
@Slf4j
@RestController
@RequestMapping("/kpnsiteapp")
@Api(tags = "站点app更新配置")
public class KpnSiteAppController {
    @Autowired
    private IKpnSiteAppService kpnSiteAppService;

    /**
     * 列表
     */
    @ApiOperation(value = "查询列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "分页起始位置", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "分页结束位置", required = true, dataType = "Integer")
    })
    @GetMapping
    public Result<PageResult<KpnSiteApp>> list(@RequestParam Map<String, Object> params,@LoginUser SysUser user) {
        return Result.succeed(kpnSiteAppService.findList(params,user));
    }

    /**
     * 查询
     */
    @ApiOperation(value = "查询")
    @GetMapping("/{id}")
    public Result findUserById(@PathVariable Long id) {
        KpnSiteApp model = kpnSiteAppService.getById(id);
        return Result.succeed(model, "查询成功");
    }

    /**
     * 新增or更新
     */
    @ApiOperation(value = "保存")
    @PostMapping
    public Result save(@RequestBody KpnSiteApp kpnSiteApp) {
        kpnSiteAppService.saveOrUpdate(kpnSiteApp);
        return Result.succeed("保存成功");
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        kpnSiteAppService.removeById(id);
        return Result.succeed("删除成功");
    }
}

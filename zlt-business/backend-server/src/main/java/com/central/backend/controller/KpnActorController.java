package com.central.backend.controller;

import java.util.Map;

import com.central.backend.service.IKpnActorService;
import com.central.common.annotation.LoginUser;
import com.central.common.model.KpnActor;
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
 * 演员列表
 *
 * @author yixiu
 * @date 2023-02-03 16:31:09
 */
@Slf4j
@RestController
@RequestMapping("/kpnactor")
@Api(tags = "演员列表")
public class KpnActorController {
    @Autowired
    private IKpnActorService kpnActorService;

    /**
     * 列表
     */
    @ApiOperation(value = "查询列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "演员ID", required = false, dataType = "Long"),
            @ApiImplicitParam(name = "nameZh", value = "中文名", required = false, dataType = "String"),
            @ApiImplicitParam(name = "nameEn", value = "英文名", required = false, dataType = "String"),
            @ApiImplicitParam(name = "nameKh", value = "柬文名", required = false, dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "起始时间查询", required = false),
            @ApiImplicitParam(name = "endTime", value = "结束时间查询", required = false),
            @ApiImplicitParam(name = "page", value = "分页起始位置", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "分页结束位置", required = true, dataType = "Integer")
    })
    @GetMapping
    public PageResult list(@RequestParam Map<String, Object> params,@LoginUser SysUser user) {
        return kpnActorService.findList(params,user);
    }

    /**
     * 查询
     */
    @ApiOperation(value = "查询演员详情")
    @GetMapping("/{id}")
    public Result findUserById(@PathVariable Long id) {
        KpnActor model = kpnActorService.getById(id);
        return Result.succeed(model, "查询成功");
    }

    /**
     * 新增or更新
     */
    @ApiOperation(value = "添加演员或修改演员")
    @PostMapping
    public Result save(@RequestBody KpnActor kpnActor) {
        kpnActorService.saveOrUpdate(kpnActor);
        return Result.succeed("保存成功");
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        kpnActorService.removeById(id);
        return Result.succeed("删除成功");
    }
}

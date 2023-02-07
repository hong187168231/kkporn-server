package com.central.backend.controller;

import com.central.backend.co.EnabledUserCo;
import com.central.backend.co.GaBindCo;
import com.central.backend.service.ISysUserService;
import com.central.common.annotation.LoginUser;
import com.central.common.model.PageResult;
import com.central.common.model.Result;
import com.central.common.model.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Api(tags = "管理员中心")
@Slf4j
@RequestMapping("/platform/admin")
public class AdminController {

    private static final String ADMIN_CHANGE_MSG = "超级管理员不给予修改";

    @Autowired
    private ISysUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 管理员列表
     */
    @ApiOperation(value = "查询管理员列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "管理员账号", required = false, dataType = "String"),
            @ApiImplicitParam(name = "enabled", value = "状态：0禁用，1启用", required = false, dataType = "String"),
            @ApiImplicitParam(name = "type", value = "APP：前端app用户，BACKEND：后端管理用户", required = false, dataType = "String"),
            @ApiImplicitParam(name = "page", value = "分页起始位置", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "分页结束位置", required = true, dataType = "Integer")
    })
    @GetMapping
    public PageResult list(@RequestParam Map<String, Object> params, @LoginUser SysUser user) {
        return userService.findList(params,user);
    }


    /**
     * 清除缓存
     */
    public void cacheEvictUser(Long id) {
        SysUser sysUser = userService.selectById(id);
        if (sysUser != null) {
            userService.cacheEvictUser(sysUser);
        }
    }


    /**
     * 删除用户
     *
     * @param id
     */
    @DeleteMapping(value = "/users/delete/{id}")
    @ApiOperation(value = "删除用户")
    public Result delete(@PathVariable Long id) {
        if (checkAdmin(id)) {
            return Result.failed(ADMIN_CHANGE_MSG);
        }
        cacheEvictUser(id);
        userService.delUser(id);
        return Result.succeed("删除成功");
    }

    /**
     * 修改状态
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "修改状态")
    @GetMapping("/users/updateEnabled")
    public Result updateEnabled(@ModelAttribute EnabledUserCo params) {
        Long id = params.getId();
        if (checkAdmin(id)) {
            return Result.failed(ADMIN_CHANGE_MSG);
        }
        cacheEvictUser(id);
        return userService.updateEnabled(params);
    }


    /**
     * 谷歌验证码是否校验状态修改
     */
    @ApiOperation(value = "谷歌验证码是否校验状态修改")
    @PutMapping(value = "/users/{id}/updateVerify")
    public Result updateVerify(@PathVariable Long id) {
        if (checkAdmin(id)) {
            return Result.failed(ADMIN_CHANGE_MSG);
        }
        cacheEvictUser(id);
        return userService.updateVerify(id);
    }

    /**
     * 是否超级管理员
     */
    private boolean checkAdmin(long id) {
        return id == 1L;
    }

    /**
     * 重置密码
     */
    @ApiOperation(value = "重置密码")
    @PutMapping(value = "/users/{id}/password")
    public Result resetPasswords(@PathVariable Long id) {
        if (checkAdmin(id)) {
            return Result.failed(ADMIN_CHANGE_MSG);
        }
        cacheEvictUser(id);
        String password = userService.resetUpdatePassword(id);
        return Result.succeed(password, "重置成功");
    }

    @ApiOperation(value = "修改密码")
    @PutMapping(value = "/users/password")
    public Result updatePassword(@RequestBody SysUser sysUser) {

        if (checkAdmin(sysUser.getId())) {
            return Result.failed(ADMIN_CHANGE_MSG);
        }
        cacheEvictUser(sysUser.getId());
        userService.updatePassword(sysUser.getId(), sysUser.getOldPassword(), sysUser.getNewPassword());
        return Result.succeed("重置成功");
    }

    /**
     * 重置谷歌验证码
     */
    @ApiOperation(value = "重置谷歌验证码")
    @PutMapping(value = "/users/{id}/resetGoogleCode")
    public Result resetGoogleCode(@PathVariable Long id) {
        GaBindCo param = new GaBindCo();
        param.setId(id);
        param.setGaBind(2);

        cacheEvictUser(param.getId());

        Result result = userService.updateGaBind(param);
        if (result != null && result.getResp_code() == 0) {
            return Result.succeed();
        }
        return Result.failed("重置失败");
    }
}

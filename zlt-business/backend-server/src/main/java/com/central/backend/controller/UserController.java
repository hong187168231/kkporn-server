package com.central.backend.controller;


import com.central.backend.co.*;
import com.central.backend.service.ISysUserService;
import com.central.common.annotation.LoginUser;
import com.central.common.model.PageResult;
import com.central.common.model.Result;
import com.central.common.model.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Slf4j
@RestController
@Api(tags = "会员管理api")
@Validated
@RequestMapping("/user")
public class UserController {


    @Autowired
    private ISysUserService userService;



    @ApiOperation("查询会员列表")
    @ResponseBody
    @GetMapping("/findUserList")
    public Result<PageResult<SysUser>> findUserList(@ModelAttribute SysUserCo params) {
        PageResult<SysUser> userList = userService.findUserList(params);
        return Result.succeed(userList);
    }

    /**
     * 新增or更新
     *
     * @param sysUser
     * @return
     */
    @ApiOperation("新增or更新")
    @PostMapping("/saveOrUpdateUserInfo")
    public Result saveOrUpdateUserInfo(@RequestBody SysUser user, @LoginUser SysUser sysUser) {

    /*    if (user.getId() == null) {
            user.setUpdateBy(sysUser.getUsername());
            user.setCreateBy(sysUser.getUsername());
        }else {
            user.setUpdateBy(sysUser.getUsername());
        }*/
        return userService.saveOrUpdateUserInfo(user);
    }


    /**
     * 重置登录密码
     * @param id
     */
    @ApiOperation("重置登录密码")
    @PutMapping(value = "/password/{id}")
    public Result resetPassword(@PathVariable Long id) {
        String password = userService.resetUpdatePassword(id);
        return Result.succeed(password, "重置成功");
    }



    @ApiOperation(value = "修改会员状态")
    @GetMapping("/updateEnabled")
    public Result updateEnabled(@Valid @ModelAttribute EnabledUserCo params, @LoginUser SysUser sysUser) {
        // params.setUpdateBy(sysUser.getUsername());
        Result result = userService.updateEnabled(params);
        return result;
    }

}

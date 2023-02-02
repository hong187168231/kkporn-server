package com.central.backend.controller;

import com.central.backend.service.IKpnSiteSignService;
import com.central.common.annotation.LoginUser;
import com.central.common.model.*;
import com.central.common.model.KpnSiteSign;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "签到配置api")
@Validated
@RequestMapping("/sign")
public class KpnSiteSignController {

    @Autowired
    private IKpnSiteSignService signService;



    /* 查询签到列表
     * @Author: Lulu
     * @Date: 2023/2/2
     */
    @ApiOperation("查询签到列表")
    @ResponseBody
    @GetMapping("/findSignList")
    public Result< List<KpnSiteSign>> findSignList( String siteCode) {
        List<KpnSiteSign> signList = signService.findSignList(siteCode);
        return Result.succeed(signList);
    }


    @ApiOperation(value = "保存签到配置")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdateSign(@RequestBody List<KpnSiteSign> list, @LoginUser SysUser sysUser) throws Exception {
        Boolean aBoolean = signService.saveOrUpdateSign(list);
        return aBoolean ? Result.succeed("操作成功") : Result.failed("操作失败");
    }



}

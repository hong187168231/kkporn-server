package com.central.backend.controller;

import com.central.backend.service.IKpnSiteSignService;
import com.central.common.annotation.LoginUser;
import com.central.common.model.*;
import com.central.common.model.KpnSiteSign;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "siteId", value = "站点id", required = true, dataType = "Long"),
    })
    public Result< List<KpnSiteSign>> findSignList(@RequestParam("siteId") Long siteId) {
        List<KpnSiteSign> signList = signService.findSignList(siteId);
        return Result.succeed(signList);
    }


    @ApiOperation(value = "保存签到配置")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody List<KpnSiteSign> list) {
        Boolean aBoolean = signService.saveOrUpdateSign(list);
        return aBoolean ? Result.succeed("操作成功") : Result.failed("操作失败");
    }



}

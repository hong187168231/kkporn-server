package com.central.backend.controller;

import com.central.backend.service.IKpnSitePlatformService;
import com.central.common.model.KpnSitePlatform;
import com.central.common.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(tags = "平台配置api")
@Validated
@RequestMapping("/platform")
public class KpnSitePlatformController {

    @Autowired
    private IKpnSitePlatformService sitePlatformService;


    /* 查询平台配置
     * @Author: Lulu
     * @Date: 2023/2/24
     */
    @ApiOperation("查询平台配置")
    @ResponseBody
    @GetMapping("/findPlatformInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "siteId", value = "站点id", required = true, dataType = "Long"),
    })
    public Result<KpnSitePlatform> findPromotionInfo(@RequestParam("siteId") Long siteId) {
        KpnSitePlatform promotion = sitePlatformService.findPromotionInfo(siteId);
        return Result.succeed(promotion);
    }




    @ApiOperation(value = "保存配置")
    @PostMapping("/saveOrUpdatePlatform")
    public Result saveOrUpdatePlatform(@RequestBody KpnSitePlatform info) {
        Boolean aBoolean = sitePlatformService.saveOrUpdatePlatform(info);
        return aBoolean ? Result.succeed("操作成功") : Result.failed("操作失败");
    }
}

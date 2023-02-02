package com.central.porn.controller;

import cn.hutool.core.util.ObjectUtil;
import com.central.common.constant.PornConstants;
import com.central.common.model.KpnSite;
import com.central.common.model.Result;
import com.central.porn.service.IKpnSiteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * API
 */
@Slf4j
@RestController
@RequestMapping("/v1/site")
@Api(tags = "站点接口")
public class PornController {

    @Autowired
    private IKpnSiteService kpnSiteService;

    @GetMapping("/info")
    @ApiOperation(value = "获取站点信息")
    public Result<KpnSite> get(HttpServletRequest request) {
        String referer = request.getHeader(PornConstants.Str.Referer);
        String host = request.getHeader(PornConstants.Str.REHost);
        log.info("referer: {},host: {}", referer, host);

        KpnSite kpnSite = kpnSiteService.getInfoByReferer(referer);
        if (ObjectUtil.isEmpty(kpnSite)) {
            kpnSite = kpnSiteService.getInfoByReferer(host);
        }
        return Result.succeed(kpnSite);
    }
}

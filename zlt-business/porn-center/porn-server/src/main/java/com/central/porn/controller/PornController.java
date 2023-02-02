package com.central.porn.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.central.common.constant.PornConstants;
import com.central.common.dto.I18nSourceDTO;
import com.central.common.model.KpnSite;
import com.central.common.model.KpnSiteChannel;
import com.central.common.model.Result;
import com.central.common.utils.I18nUtil;
import com.central.porn.entity.vo.FullSourceVo;
import com.central.porn.entity.vo.KpnSiteChannelVo;
import com.central.porn.entity.vo.KpnSiteVo;
import com.central.porn.service.IKpnSiteChannelService;
import com.central.porn.service.IKpnSiteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * API
 */
@Slf4j
@RestController
@RequestMapping("/v1/site")
@Api(tags = "api接口")
public class PornController {

    @Autowired
    private IKpnSiteService siteService;

    @Autowired
    private IKpnSiteChannelService siteChannelService;

    /**
     * 获取站点信息
     *
     * @param request
     * @return
     */
    @GetMapping("/info")
    @ApiOperation(value = "获取站点信息")
    public Result<KpnSiteVo> get(HttpServletRequest request) {
        String referer = request.getHeader(PornConstants.Str.REFERER);
        String host = request.getHeader(PornConstants.Str.REHOST);
        String sid = request.getHeader(PornConstants.Str.SID);
        log.info("sid: {},referer: {},host: {}", sid, referer, host);

        KpnSite site = null;
        if (StrUtil.isBlank(sid)) {
            site = siteService.getInfoByReferer(referer);
            if (ObjectUtil.isEmpty(site)) {
                site = siteService.getInfoByReferer(host);
            }
        } else {
            site = siteService.getInfoById(Long.parseLong(sid));
        }

        // TODO 异常统一处理
        if(ObjectUtil.isEmpty(site)){
            throw new RuntimeException("站点不存在");
        }

        //频道
        List<KpnSiteChannel> channelList = siteChannelService.getBySiteId(site.getId());
        List<KpnSiteChannelVo> channelVos = channelList.stream().map(kpnSiteChannel -> {
            KpnSiteChannelVo kpnSiteChannelVo = new KpnSiteChannelVo();
            BeanUtil.copyProperties(kpnSiteChannel, kpnSiteChannelVo);
            return kpnSiteChannelVo;
        }).collect(Collectors.toList());

        //专题

        //站点信息
        KpnSiteVo kpnSiteVo = new KpnSiteVo();
        kpnSiteVo.setSid(site.getId());
        kpnSiteVo.setCurrencyCode(site.getCurrencyCode());
        kpnSiteVo.setLogoUrl(site.getLogoUrl());
        kpnSiteVo.setChannels(channelVos);


        return Result.succeed(kpnSiteVo);
    }

    /**
     * 获取会员频道,非会员返回站点4固定频道
     */
    @ApiOperation(value = "获取会员频道")
    @GetMapping("getChannel")
    public Result<String> getChannel() {
        try {
            return Result.succeed("succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }


    /**
     * token续期
     */
    @ApiOperation(value = "token续期,每10分钟调用一次")
    @PostMapping("/refreshAccessToken")
    public Result<String> refreshAccessToken() {
        try {
            return Result.succeed("succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    /**
     * 语言包-H5
     */
    @ApiOperation(value = "语言包-H5")
    @PostMapping("/h5FullSource")
    public Result<FullSourceVo> h5FullSource() {
        try {
            FullSourceVo fullSourceVo = new FullSourceVo();
            I18nSourceDTO frontFullSource = I18nUtil.getFrontAppFullSource();
            BeanUtil.copyProperties(frontFullSource, fullSourceVo);

            return Result.succeed(fullSourceVo, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    /**
     * 语言包-PC
     */
    @ApiOperation(value = "语言包-pc")
    @PostMapping("/pcFullSource")
    public Result<FullSourceVo> pcFullSource() {
        try {
            FullSourceVo fullSourceVo = new FullSourceVo();
            I18nSourceDTO frontFullSource = I18nUtil.getFrontFullSource();
            BeanUtil.copyProperties(frontFullSource, fullSourceVo);

            return Result.succeed(fullSourceVo, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }
}

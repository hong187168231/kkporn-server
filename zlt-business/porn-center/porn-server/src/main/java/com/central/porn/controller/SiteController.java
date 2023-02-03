package com.central.porn.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.central.common.constant.PornConstants;
import com.central.common.dto.I18nSourceDTO;
import com.central.common.model.*;
import com.central.common.utils.I18nUtil;
import com.central.porn.entity.vo.*;
import com.central.porn.service.IKpnSiteAdvertiseService;
import com.central.porn.service.IKpnSiteChannelService;
import com.central.porn.service.IKpnSiteService;
import com.central.porn.service.IKpnSiteTopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * 站点相关
 */
@Slf4j
@RestController
@RequestMapping("/v1/site")
@Api(tags = "站点相关api接口")
public class SiteController {

    @Autowired
    private IKpnSiteService siteService;

    @Autowired
    private IKpnSiteChannelService siteChannelService;

    @Autowired
    private IKpnSiteTopicService siteTopicService;

    @Autowired
    private IKpnSiteAdvertiseService siteAdvertiseService;

    /**
     * 获取站点信息
     *
     * @param request
     * @return
     */
    @GetMapping("/info")
    @ApiOperation(value = "获取站点信息")
    public Result<KpnSiteVo> getSiteInfo(HttpServletRequest request) {
        try {
            String referer = request.getHeader(PornConstants.Str.REFERER);
            String host = request.getHeader(PornConstants.Str.REHOST);
            String sid = request.getHeader(PornConstants.Str.SID);
            log.info("获取站点信息 -> sid: {},referer: {},host: {}", sid, referer, host);

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
            if (ObjectUtil.isEmpty(site)) {
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
            List<KpnSiteTopic> siteTopicList = siteTopicService.getBySiteId(site.getId());
            List<KpnSiteTopicVo> topicVos = siteTopicList.stream().map(kpnSiteTopic -> {
                KpnSiteTopicVo topicVo = new KpnSiteTopicVo();
                BeanUtil.copyProperties(kpnSiteTopic, topicVo);
                return topicVo;
            }).collect(Collectors.toList());

            //站点信息
            KpnSiteVo kpnSiteVo = new KpnSiteVo();
            kpnSiteVo.setSid(site.getId());
            kpnSiteVo.setCurrencyCode(site.getCurrencyCode());
            kpnSiteVo.setLogoUrl(site.getLogoUrl());
            kpnSiteVo.setChannels(channelVos);
            kpnSiteVo.setTopics(topicVos);

            return Result.succeed(kpnSiteVo, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    /**
     * 获取站点广告
     *
     * @return
     */
    @GetMapping("/ads")
    @ApiOperation(value = "获取站点信息")
    public Result<Map<String, Map<Integer, List<KpnSiteAdvertiseVo>>>> getSiteAdvertise(@RequestHeader(value = "sid") Long sid) {
        try {
            List<KpnSiteAdvertise> siteAds = siteAdvertiseService.getSiteAdvertise(sid);
            List<KpnSiteAdvertiseVo> siteAdVos = siteAds.stream().map(ad -> {
                KpnSiteAdvertiseVo adVo = new KpnSiteAdvertiseVo();
                BeanUtil.copyProperties(ad, adVo);
                return adVo;
            }).collect(Collectors.toList());

            //分组统计
            Map<String, Map<Integer, List<KpnSiteAdvertiseVo>>> siteAdVoMap = siteAdVos.stream()
                    .collect(groupingBy(KpnSiteAdvertiseVo::getDevice, groupingBy(KpnSiteAdvertiseVo::getPosition)));

            return Result.succeed(siteAdVoMap, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    /**
     * 我的频道
     */

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

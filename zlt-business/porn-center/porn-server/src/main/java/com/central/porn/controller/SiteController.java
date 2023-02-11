package com.central.porn.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.central.common.constant.PornConstants;
import com.central.common.dto.I18nSourceDTO;
import com.central.common.model.*;
import com.central.common.utils.I18nUtil;
import com.central.porn.core.language.LanguageUtil;
import com.central.porn.entity.vo.*;
import com.central.porn.enums.KpnActorSortTypeEnum;
import com.central.porn.enums.KpnSortOrderEnum;
import com.central.porn.enums.KpnStableChannelEnum;
import com.central.porn.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
    private IKpnLineService kpnLineService;

    @Autowired
    private IKpnSiteAdvertiseService siteAdvertiseService;

    @Autowired
    private IKpnSiteActorService siteActorService;

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

            //站点信息
            KpnSiteVo kpnSiteVo = new KpnSiteVo();
            kpnSiteVo.setSid(site.getId());
            kpnSiteVo.setCurrencyCode(site.getCurrencyCode());
            kpnSiteVo.setLogoUrl(site.getLogoUrl());
//            kpnSiteVo.setChannels(channelVos);
//            kpnSiteVo.setTopics(topicVos);

            return Result.succeed(kpnSiteVo, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    /**
     * 获取站点线路
     *
     * @param sid 站点id
     * @return
     */
    @GetMapping("/lines")
    @ApiOperation(value = "获取站点线路")
    public Result<Map<String, List<String>>> getLines(@RequestHeader(value = "sid", required = false) Long sid) {
        try {
            Map<String, List<String>> kpnLineVos = kpnLineService.getLines();
            return Result.succeed(kpnLineVos, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    /**
     * 获取站点频道
     *
     * @param sid 站点id
     * @return
     */
    @GetMapping("/channels")
    @ApiOperation(value = "获取站点频道")
    public Result<List<KpnSiteChannelVo>> getChannels(@RequestHeader("sid") Long sid) {
        try {
            List<KpnSiteChannel> channelList = siteChannelService.getBySiteId(sid);
            List<KpnSiteChannelVo> channelVos = channelList.stream().map(kpnSiteChannel -> {
                KpnSiteChannelVo kpnSiteChannelVo = new KpnSiteChannelVo();
                BeanUtil.copyProperties(kpnSiteChannel, kpnSiteChannelVo);
                kpnSiteChannelVo.setName(LanguageUtil.getLanguageName(kpnSiteChannelVo));
                if (KpnStableChannelEnum.RECOMMEND.getSort().equals(kpnSiteChannelVo.getSort())) {
                    kpnSiteChannelVo.setIsRecommend(true);
                }
                if (KpnStableChannelEnum.SEARCH.getSort().equals(kpnSiteChannelVo.getSort())) {
                    kpnSiteChannelVo.setIsSearch(true);
                }
                return kpnSiteChannelVo;
            }).collect(Collectors.toList());

            return Result.succeed(channelVos, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    /**
     * 获取站点专题
     *
     * @param sid 站点id
     * @return
     */
    @GetMapping("/topics")
    @ApiOperation(value = "获取站点专题")
    public Result<List<KpnSiteTopicVo>> getTopics(@RequestHeader("sid") Long sid) {
        try {
            List<KpnSiteTopicVo> siteTopicVos = siteTopicService.getBySiteId(sid);
            return Result.succeed(siteTopicVos, "succeed");
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
    @ApiOperation(value = "获取站点广告")
    public Result<Map<String, Map<Integer, List<KpnSiteAdvertiseVo>>>> getSiteAdvertise(@RequestHeader(value = "sid") Long sid) {
        try {
            List<KpnSiteAdvertise> siteAds = siteAdvertiseService.getSiteAdvertise(sid);
            List<KpnSiteAdvertiseVo> siteAdVos = siteAds.stream().map(ad -> {
                KpnSiteAdvertiseVo adVo = new KpnSiteAdvertiseVo();
                BeanUtil.copyProperties(ad, adVo);
                adVo.setName(LanguageUtil.getLanguageName(adVo));
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
     * 演员列表查询
     */
    @GetMapping("/actor/list")
    @ApiOperation(value = "演员列表")
    public Result<List<KpnActorVo>> getActorList(@RequestHeader("sid") Long sid, String sortType, Integer sortOrderCode) {
        try {
            //排序字段
            if (StrUtil.isBlank(sortType) || !KpnActorSortTypeEnum.isLegalType(sortType)) {
                sortType = KpnActorSortTypeEnum.HOT.getType();
            }

            //排序顺序
            if (ObjectUtil.isNull(sortOrderCode) || !KpnSortOrderEnum.isLegalCode(sortOrderCode)) {
                sortOrderCode = KpnSortOrderEnum.DESC.getCode();
            }

            List<KpnActorVo> actorVos = new ArrayList<>();
            //按收藏量查询
            if(sortType.equalsIgnoreCase(KpnActorSortTypeEnum.HOT.getType())){
                actorVos = siteActorService.getActorListByFavorites(sid, KpnSortOrderEnum.getByCode(sortOrderCode).name());
            }
            //按创建时间
            else if(sortType.equalsIgnoreCase(KpnActorSortTypeEnum.LATEST.getType())){
                actorVos = siteActorService.getActorListByCreateTime(sid, KpnSortOrderEnum.getByCode(sortOrderCode).name());
            }

            return Result.succeed(actorVos, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }



    /**
     * token续期
     */
//    @ApiOperation(value = "token续期,每10分钟调用一次")
//    @PostMapping("/refreshAccessToken")
//    public Result<String> refreshAccessToken() {
//        try {
//            return Result.succeed("succeed");
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            return Result.failed("failed");
//        }
//    }

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

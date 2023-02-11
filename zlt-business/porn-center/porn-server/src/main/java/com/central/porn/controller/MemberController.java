package com.central.porn.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.central.common.annotation.LoginUser;
import com.central.common.model.KpnSiteChannel;
import com.central.common.model.Result;
import com.central.common.model.SysUser;
import com.central.porn.core.language.LanguageUtil;
import com.central.porn.entity.co.MemberChannelSortCo;
import com.central.porn.entity.vo.KpnMemberChannelVo;
import com.central.porn.service.IKpnMovieService;
import com.central.porn.service.IKpnSiteActorService;
import com.central.porn.service.IKpnSiteChannelService;
import com.central.porn.service.IKpnSiteMovieService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 会员相关
 */
@Slf4j
@RestController
@RequestMapping("/v1/member")
@Api(tags = "会员相关api接口")
public class MemberController {

    @Autowired
    private IKpnSiteActorService siteActorService;


    @Autowired
    private IKpnSiteChannelService siteChannelService;


    @Autowired
    private IKpnMovieService movieService;

    @Autowired
    private IKpnSiteMovieService siteMovieService;

    /**
     * 获取会员自定义频道
     *
     * @return
     */
    @GetMapping("/channels")
    @ApiOperation(value = "获取会员自定义频道")
    public Result<List<KpnMemberChannelVo>> getUserChannels(@ApiIgnore @LoginUser SysUser user) {
        try {
            List<KpnSiteChannel> memberChannels = siteChannelService.getMemberChannels(user.getId());

            List<KpnMemberChannelVo> memberChannelVos = memberChannels.stream().map(kpnSiteChannel -> {
                KpnMemberChannelVo memberChannelVo = new KpnMemberChannelVo();
                memberChannelVo.setChannelId(kpnSiteChannel.getId());
                BeanUtil.copyProperties(kpnSiteChannel, memberChannelVo);
                memberChannelVo.setName(LanguageUtil.getLanguageName(memberChannelVo));
                return memberChannelVo;
            }).collect(Collectors.toList());

            return Result.succeed(memberChannelVos, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    /**
     * 会员自定义频道-排序
     *
     * @return
     */
    @PostMapping("/channel/sort")
    @ApiOperation(value = "会员自定义频道排序")
    public Result<String> changeChannelsSort(@ApiIgnore @LoginUser SysUser user, @RequestBody List<MemberChannelSortCo> channelSortCos) {
        try {
            siteChannelService.saveMemberChannelsSort(user.getId(), channelSortCos);
            return Result.succeed("succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    /**
     * 会员自定义频道-添加
     *
     * @param user      登录会员
     * @param channelId 添加频道id
     * @return
     */
    @PostMapping("/channel/add")
    @ApiOperation(value = "会员自定义频道-添加")
    public Result<String> addChannel(@ApiIgnore @LoginUser SysUser user, Long channelId) {
        try {
            siteChannelService.addChannel(user.getId(), user.getUsername(), user.getSiteId(), user.getSiteCode(), user.getSiteName(), channelId);
            return Result.succeed("succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    /**
     * 会员自定义频道-移除频道
     *
     * @param user      登录会员
     * @param channelId 添加频道id
     * @return
     */
    @PostMapping("/channel/remove")
    @ApiOperation(value = "会员自定义频道-移除")
    public Result<String> removeChannel(@ApiIgnore @LoginUser SysUser user, Long channelId) {
        try {
            siteChannelService.removeChannel(user.getId(), channelId);
            return Result.succeed("succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    /**
     * 会员添加影片收藏
     *
     * @return 影片收藏量
     */
    @PostMapping("/favorites/add/movie")
    @ApiOperation(value = "会员添加影片收藏")
    public Result<Long> addMovieFavorites(@ApiIgnore @LoginUser SysUser user, Long movieId) {
        try {
            Long siteMovieFavorites = siteMovieService.addSiteMovieFavorites(user.getSiteId(), user.getId(), movieId);

            return Result.succeed(siteMovieFavorites, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    /**
     * 会员移除影片收藏
     *
     * @return 影片收藏量
     */
    @PostMapping("/favorites/remove/movie")
    @ApiOperation(value = "取消影片收藏")
    public Result<Long> removeMovieFavorites(@ApiIgnore @LoginUser SysUser user, Long movieId) {
        try {
            Long siteMovieFavorites = siteMovieService.removeSiteMovieFavorites(user.getSiteId(), user.getId(), movieId);

            return Result.succeed(siteMovieFavorites, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }


    /**
     * 会员添加演员收藏
     *
     * @return 站点演员收藏量
     */
    @PostMapping("/favorites/add/actor")
    @ApiOperation(value = "会员添加演员收藏")
    public Result<Long> addActorFavorites(@ApiIgnore @LoginUser SysUser user, Long actorId) {
        try {
            if (ObjectUtil.isEmpty(user)) {
                return Result.failed("请先登录");
            }

            Long siteActorFavorites = siteActorService.addSiteActorFavorites(user.getSiteId(), user.getId(), actorId);

            return Result.succeed(siteActorFavorites, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }

    /**
     * 会员移除演员收藏
     *
     * @return 演员收藏量
     */
    @PostMapping("/favorites/remove/actor")
    @ApiOperation(value = "取消演员收藏")
    public Result<Long> removeActorFavorites(@ApiIgnore @LoginUser SysUser user, Long actorId) {
        try {
            if (ObjectUtil.isEmpty(user)) {
                return Result.failed("请先登录");
            }
            Long siteActorFavorites = siteActorService.removeSiteActorFavorites(user.getSiteId(), user.getId(), actorId);

            return Result.succeed(siteActorFavorites, "succeed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.failed("failed");
        }
    }


}

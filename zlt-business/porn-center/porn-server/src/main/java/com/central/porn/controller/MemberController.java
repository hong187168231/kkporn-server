package com.central.porn.controller;

import cn.hutool.core.bean.BeanUtil;
import com.central.common.annotation.LoginUser;
import com.central.common.model.KpnSiteChannel;
import com.central.common.model.Result;
import com.central.common.model.SysUser;
import com.central.porn.entity.co.MemberChannelSortCo;
import com.central.porn.entity.vo.KpnMemberChannelVo;
import com.central.porn.service.IKpnSiteChannelService;
import com.central.porn.service.IKpnSiteUserService;
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
    private IKpnSiteUserService siteUserService;

    @Autowired
    private IKpnSiteChannelService siteChannelService;

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
    @PostMapping("/changeChannelsSort")
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
    @PostMapping("/addChannel")
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
    @PostMapping("/removeChannel")
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


}

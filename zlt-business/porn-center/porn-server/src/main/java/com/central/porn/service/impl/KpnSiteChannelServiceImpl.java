package com.central.porn.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.central.common.constant.PornConstants;
import com.central.common.model.KpnSiteChannel;
import com.central.common.model.KpnSiteUserChannel;
import com.central.common.redis.template.RedisRepository;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.entity.co.MemberChannelSortCo;
import com.central.porn.mapper.KpnSiteChannelMapper;
import com.central.porn.service.IKpnSiteChannelService;
import com.central.porn.service.IKpnSiteUserChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Slf4j
@Service
public class KpnSiteChannelServiceImpl extends SuperServiceImpl<KpnSiteChannelMapper, KpnSiteChannel> implements IKpnSiteChannelService {

    @Autowired
    private IKpnSiteUserChannelService userChannelService;

    @Override
    public List<KpnSiteChannel> getBySiteId(Long sid) {
        String channelRedisKey = StrUtil.format(PornConstants.RedisKey.SITE_CHANNEL_KEY, sid);
        List<KpnSiteChannel> siteChannels = (List<KpnSiteChannel>) RedisRepository.get(channelRedisKey);
        if (CollectionUtil.isEmpty(siteChannels)) {
            siteChannels = this.lambdaQuery()
                    .eq(KpnSiteChannel::getSiteId, sid)
                    .or()
                    .eq(KpnSiteChannel::getIsStable, true)
                    .orderByDesc(KpnSiteChannel::getIsStable)
                    .orderByDesc(KpnSiteChannel::getSort)
                    .orderByDesc(KpnSiteChannel::getCreateTime)
                    .list();
            if (CollectionUtil.isNotEmpty(siteChannels)) {
                RedisRepository.setExpire(channelRedisKey, siteChannels, PornConstants.RedisKey.EXPIRE_TIME_30_DAYS);
            }
        }
        return siteChannels;
    }

    @Override
    public List<KpnSiteChannel> getMemberChannels(Long uid) {
        String userChannelRedisKey = StrUtil.format(PornConstants.RedisKey.SITE_USER_CHANNEL_KEY, uid);
        List<KpnSiteChannel> memberChannels = (List<KpnSiteChannel>) RedisRepository.get(userChannelRedisKey);
        if (CollectionUtil.isEmpty(memberChannels)) {
            memberChannels = this.baseMapper.getMemberChannels(uid);
            if (CollectionUtil.isNotEmpty(memberChannels)) {
                RedisRepository.setExpire(userChannelRedisKey, memberChannels, PornConstants.RedisKey.EXPIRE_TIME_30_DAYS);
            }
        }
        return memberChannels;
    }

    @Override
    @Async
    public void saveMemberChannelsSort(Long uid, @RequestBody List<MemberChannelSortCo> channelSortCos) {
        String userChannelRedisKey = StrUtil.format(PornConstants.RedisKey.SITE_USER_CHANNEL_KEY, uid);
        RedisRepository.delete(userChannelRedisKey);

        if (CollectionUtil.isNotEmpty(channelSortCos)) {
            for (MemberChannelSortCo channelSortCo : channelSortCos) {
                userChannelService.lambdaUpdate()
                        .eq(KpnSiteUserChannel::getUserId, uid)
                        .eq(KpnSiteUserChannel::getChannelId, channelSortCo.getChannelId())
                        .set(KpnSiteUserChannel::getSort, channelSortCo.getSort())
                        .update();

            }
        }
        //重新缓存
        getMemberChannels(uid);
        log.info("频道排序完成 userId: {}", uid);
    }

    @Override
    @Async
    @Transactional
    public void addChannel(Long uid, String username, Long siteId, String siteCode, String siteName, Long channelId) {
        //增加排序值
        userChannelService.lambdaUpdate().eq(KpnSiteUserChannel::getUserId, uid)
                .setSql(" `sort` = `sort` + 1")
                .update();

        //新增频道
        KpnSiteUserChannel siteUserChannel = new KpnSiteUserChannel();
        siteUserChannel.setSiteId(siteId);
        siteUserChannel.setSiteCode(siteCode);
        siteUserChannel.setSiteName(siteName);
        siteUserChannel.setChannelId(channelId);
        siteUserChannel.setUserId(uid);
        siteUserChannel.setUserName(username);
        siteUserChannel.setChannelId(channelId);
        siteUserChannel.setSort(PornConstants.Numeric.DEFAULT_SORT_VALUE);
        userChannelService.save(siteUserChannel);

        //重置缓存
        String userChannelRedisKey = StrUtil.format(PornConstants.RedisKey.SITE_USER_CHANNEL_KEY, uid);
        RedisRepository.delete(userChannelRedisKey);
        getMemberChannels(uid);
        log.info("频道添加完成 userId: {},channelId: {}", uid, channelId);
    }

    @Override
    @Async
    @Transactional
    public void removeChannel(Long uid, Long channelId) {
        userChannelService.lambdaUpdate()
                .eq(KpnSiteUserChannel::getUserId, uid)
                .eq(KpnSiteUserChannel::getChannelId, channelId)
                .remove();

        //重置缓存
        String userChannelRedisKey = StrUtil.format(PornConstants.RedisKey.SITE_USER_CHANNEL_KEY, uid);
        RedisRepository.delete(userChannelRedisKey);
        getMemberChannels(uid);
        log.info("频道移除完成 userId: {},channelId: {}", uid, channelId);
    }


}

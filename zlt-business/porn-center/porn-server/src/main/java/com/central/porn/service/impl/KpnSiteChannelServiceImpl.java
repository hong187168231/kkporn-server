package com.central.porn.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.central.common.constant.PornConstants;
import com.central.common.model.KpnSiteChannel;
import com.central.common.redis.template.RedisRepository;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.mapper.KpnSiteChannelMapper;
import com.central.porn.service.IKpnSiteChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class KpnSiteChannelServiceImpl extends SuperServiceImpl<KpnSiteChannelMapper, KpnSiteChannel> implements IKpnSiteChannelService {

    @Override
    public List<KpnSiteChannel> getBySiteId(Long siteId) {
        String channelRedisKey = StrUtil.format(PornConstants.RedisKey.SITE_CHANNEL_KEY, siteId);
        List<KpnSiteChannel> siteChannels = (List<KpnSiteChannel>) RedisRepository.get(channelRedisKey);
        if (CollectionUtil.isEmpty(siteChannels)) {
            siteChannels = this.lambdaQuery()
                    .eq(KpnSiteChannel::getSiteId, siteId)
                    .or()
                    .eq(KpnSiteChannel::getIsStable, true)
                    .orderByDesc(KpnSiteChannel::getIsStable)
                    .orderByDesc(KpnSiteChannel::getSort)
                    .orderByDesc(KpnSiteChannel::getCreateTime)
                    .list();

            RedisRepository.set(channelRedisKey, siteChannels);
        }
        return siteChannels;
    }


}

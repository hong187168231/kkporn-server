package com.central.porn.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.central.common.constant.PornConstants;
import com.central.common.model.KpnSiteAdvertise;
import com.central.common.redis.template.RedisRepository;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.mapper.KpnSiteAdvertiseMapper;
import com.central.porn.service.IKpnSiteAdvertiseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class KpnSiteAdvertiseServiceImpl extends SuperServiceImpl<KpnSiteAdvertiseMapper, KpnSiteAdvertise> implements IKpnSiteAdvertiseService {

    @Override
    public List<KpnSiteAdvertise> getSiteAdvertise(Long sid) {
        String adRedisKey = StrUtil.format(PornConstants.RedisKey.SITE_ADVERTISE_KEY, sid);
        List<KpnSiteAdvertise> siteAds = (List<KpnSiteAdvertise>) RedisRepository.get(adRedisKey);

        if (CollectionUtil.isEmpty(siteAds)) {
            siteAds = this.lambdaQuery()
                    .eq(KpnSiteAdvertise::getSiteId, sid)
                    .eq(KpnSiteAdvertise::getStatus, true)
                    .le(KpnSiteAdvertise::getStartTime, new Date())
                    .ge(KpnSiteAdvertise::getEndTime, new Date())
                    .list();

            RedisRepository.set(adRedisKey, siteAds);
        }

        return siteAds;
    }
}

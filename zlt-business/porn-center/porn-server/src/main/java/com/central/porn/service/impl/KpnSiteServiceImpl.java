package com.central.porn.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.central.common.constant.PornConstants;
import com.central.common.model.KpnSite;
import com.central.common.redis.template.RedisRepository;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.mapper.KpnSiteMapper;
import com.central.porn.service.IKpnSiteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KpnSiteServiceImpl extends SuperServiceImpl<KpnSiteMapper, KpnSite> implements IKpnSiteService {
    @Override
    public KpnSite getInfoByReferer(String referer) {
        KpnSite kpnSite = this.lambdaQuery().like(KpnSite::getDomains, referer).one();
        return kpnSite;
    }

    @Override
    public KpnSite getInfoById(Long sid) {
        String siteKey = StrUtil.format(PornConstants.RedisKey.SITE_INFO_KEY, sid);
        KpnSite kpnSite = (KpnSite) RedisRepository.get(siteKey);
        if (ObjectUtil.isEmpty(kpnSite)) {
            kpnSite = getById(sid);

            RedisRepository.set(siteKey, kpnSite);
        }
        return kpnSite;
    }
}

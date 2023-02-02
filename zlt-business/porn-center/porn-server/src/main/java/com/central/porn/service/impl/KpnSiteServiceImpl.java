package com.central.porn.service.impl;

import com.central.common.model.KpnSite;
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
        return this.lambdaQuery().like(KpnSite::getDomains, referer).one();
    }
}

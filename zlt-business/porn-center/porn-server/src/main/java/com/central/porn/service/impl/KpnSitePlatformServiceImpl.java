package com.central.porn.service.impl;

import com.central.common.model.KpnSitePlatform;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.mapper.KpnSitePlatformMapper;
import com.central.porn.service.IKpnSitePlatformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KpnSitePlatformServiceImpl extends SuperServiceImpl<KpnSitePlatformMapper, KpnSitePlatform> implements IKpnSitePlatformService {


    @Override
    public KpnSitePlatform getBySiteId(Long sid) {
        return this.lambdaQuery().eq(KpnSitePlatform::getSiteId, sid).one();
    }
}

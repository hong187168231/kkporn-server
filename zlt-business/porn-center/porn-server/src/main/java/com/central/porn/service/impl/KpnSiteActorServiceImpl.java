package com.central.porn.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.central.common.constant.PornConstants;
import com.central.common.model.KpnActor;
import com.central.common.model.KpnSiteActor;
import com.central.common.redis.template.RedisRepository;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.mapper.KpnActorMapper;
import com.central.porn.mapper.KpnSiteActorMapper;
import com.central.porn.service.IKpnActorService;
import com.central.porn.service.IKpnSiteActorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KpnSiteActorServiceImpl extends SuperServiceImpl<KpnSiteActorMapper, KpnSiteActor> implements IKpnSiteActorService {

    @Override
    public Long getSiteActorFavorites(Long sid, Long movieId) {
        return null;
    }
}

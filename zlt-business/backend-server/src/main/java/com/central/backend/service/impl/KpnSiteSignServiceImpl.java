package com.central.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.central.backend.mapper.KpnSiteSignMapper;
import com.central.backend.service.IKpnSiteSignService;
import com.central.common.model.KpnSiteSign;
import com.central.common.service.impl.SuperServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@CacheConfig(cacheNames = {"KpnSiteSign"})
public class KpnSiteSignServiceImpl extends SuperServiceImpl<KpnSiteSignMapper, KpnSiteSign> implements IKpnSiteSignService {

    @Override
    public List<KpnSiteSign> findSignList(String siteCode) {
        LambdaQueryWrapper<KpnSiteSign> wrapper=new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(siteCode)){
            wrapper.eq(KpnSiteSign::getSiteCode,siteCode);
        }
        return baseMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdateSign(List<KpnSiteSign> list) {
        return super.saveOrUpdateBatch(list);
    }
}
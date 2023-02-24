package com.central.porn.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.central.common.model.KpnSiteOrder;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.mapper.KpnSiteOrderMapper;
import com.central.porn.service.IKpnSiteOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KpnSiteOrderServiceImpl extends SuperServiceImpl<KpnSiteOrderMapper, KpnSiteOrder> implements IKpnSiteOrderService {

    @Override
    public boolean isOrderNoExists(Long siteId, String orderNo) {
        KpnSiteOrder siteOrder = this.lambdaQuery().select(KpnSiteOrder::getSiteId).eq(KpnSiteOrder::getSiteId, siteId).eq(KpnSiteOrder::getOrderNo, orderNo).one();

        return ObjectUtil.isNotEmpty(siteOrder);
    }
}
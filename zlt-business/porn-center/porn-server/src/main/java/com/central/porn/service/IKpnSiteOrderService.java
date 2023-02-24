package com.central.porn.service;

import com.central.common.model.KpnSiteOrder;
import com.central.common.service.ISuperService;

public interface IKpnSiteOrderService extends ISuperService<KpnSiteOrder> {

    /**
     * 订单是否已经存在
     *
     * @param siteId  站点id
     * @param orderNo 订单号
     * @return
     */
    boolean isOrderNoExists(Long siteId, String orderNo);
}

package com.central.backend.service;

import com.central.backend.co.KpnSiteOrderCo;
import com.central.backend.co.KpnSiteOrderTotalCo;
import com.central.backend.co.KpnSiteOrderUpdateCo;
import com.central.common.model.KpnSiteOrder;
import com.central.common.model.PageResult;
import com.central.common.model.Result;
import com.central.common.service.ISuperService;

import java.math.BigDecimal;
import java.util.List;


/*
 * @Author: Lulu
 * @Date: 2023/2/8
 */
public interface IKpnSiteOrderService extends ISuperService<KpnSiteOrder> {

    PageResult<KpnSiteOrder> findOrderList( KpnSiteOrderCo params) ;

    BigDecimal findOrderTotal(KpnSiteOrderTotalCo params);

    Result updateStatus(KpnSiteOrderUpdateCo params);

    List<KpnSiteOrder> findOrderMobileList(List<Long> userIds) ;
}

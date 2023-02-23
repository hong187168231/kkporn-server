package com.central.backend.mapper;

import com.central.backend.co.KpnSiteOrderTotalCo;
import com.central.common.model.KpnSiteOrder;
import com.central.common.model.KpnSiteUserVipLog;
import com.central.db.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;


/*
 * @Author: Lulu
 * @Date: 2023/2/8
 */
@Mapper
public interface KpnSiteOrderMapper extends SuperMapper<KpnSiteOrder> {

    BigDecimal findOrderTotal( @Param("r") KpnSiteOrderTotalCo params);

    List<KpnSiteOrder> findOrderMobileList(@Param("userIds") List<Long> userIds);


    KpnSiteUserVipLog findKpnSiteOrderInfo(@Param("id") Long id);

}

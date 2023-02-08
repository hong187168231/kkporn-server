package com.central.backend.mapper;

import com.central.backend.co.KpnSiteOrderTotalCo;
import com.central.common.model.KpnSiteOrder;
import com.central.db.mapper.SuperMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;


/*
 * @Author: Lulu
 * @Date: 2023/2/8
 */
@Mapper
public interface KpnSiteOrderMapper extends SuperMapper<KpnSiteOrder> {

    BigDecimal findOrderTotal( @Param("r") KpnSiteOrderTotalCo params);

}

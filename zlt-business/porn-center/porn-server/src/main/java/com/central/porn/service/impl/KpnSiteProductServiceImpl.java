package com.central.porn.service.impl;

import com.central.common.model.pay.KpnSiteProduct;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.mapper.KpnSiteProductMapper;
import com.central.porn.service.IKpnSiteProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KpnSiteProductServiceImpl extends SuperServiceImpl<KpnSiteProductMapper, KpnSiteProduct> implements IKpnSiteProductService {
}

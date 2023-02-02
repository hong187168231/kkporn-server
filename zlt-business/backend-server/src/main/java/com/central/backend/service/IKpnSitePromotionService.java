package com.central.backend.service;

import com.central.common.model.KpnSitePromotion;
import com.central.common.service.ISuperService;

public interface IKpnSitePromotionService extends ISuperService<KpnSitePromotion> {




    KpnSitePromotion findPromotionInfo(String siteCode);


    Boolean saveOrUpdatePromotion(KpnSitePromotion info);
}

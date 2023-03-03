package com.central.porn.service;

import com.central.common.model.KpnSiteAdvertise;
import com.central.common.service.ISuperService;

import java.util.List;


public interface IKpnSiteAdvertiseService extends ISuperService<KpnSiteAdvertise> {

    /**
     * 获取站点广告
     *
     * @param sid 站点id
     * @return
     */
    List<KpnSiteAdvertise> getSiteAdvertise(Long sid);

    /**
     * 增加广告点击量
     * @param adId
     */
    void addHits(Long adId);
}

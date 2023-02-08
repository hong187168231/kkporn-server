package com.central.porn.service;

import com.central.common.model.KpnSiteActor;
import com.central.common.service.ISuperService;


public interface IKpnSiteActorService extends ISuperService<KpnSiteActor> {
    /**
     * 获取站点影片播放量
     *
     * @param sid     站点id
     * @param movieId 影片id
     * @return
     */
    Long getSiteActorFavorites(Long sid, Long movieId);
}

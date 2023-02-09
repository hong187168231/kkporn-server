package com.central.porn.service;

import com.central.common.model.KpnSiteActor;
import com.central.common.service.ISuperService;


public interface IKpnSiteActorService extends ISuperService<KpnSiteActor> {
    /**
     * 获取站点演员收藏量
     *
     * @param sid     站点id
     * @param actorId 演员id
     * @return
     */
    Long getSiteActorFavorites(Long sid, Long actorId);

    /**
     * 收藏演员
     *
     * @param sid  站点id
     * @param userId  会员id
     * @param actorId 演员id
     * @return 最新收藏量
     */
    Long addSiteActorFavorites(Long sid, Long userId, Long actorId);

    /**
     * 取消演员收藏
     *
     * @param sid  站点id
     * @param userId  会员id
     * @param actorId 演员id
     * @return 最新收藏量
     */
    Long removeSiteActorFavorites(Long sid, Long userId, Long actorId);
}

package com.central.porn.service;

import com.central.common.model.KpnSiteUserActorFavorites;
import com.central.common.service.ISuperService;


public interface IKpnSiteUserActorFavoritesService extends ISuperService<KpnSiteUserActorFavorites> {

    /**
     * 获取会员收藏的影片
     *
     * @param userId  会员id
     * @param actorId 演员id
     * @return
     */
    KpnSiteUserActorFavorites getUserActorFavorites(Long userId, Long actorId);

    /**
     * 增加收藏
     *
     * @param userId  会员id
     * @param actorId 演员id
     */
    void add(Long userId, Long actorId);

    /**
     * 移除收藏
     *
     * @param userId  会员id
     * @param actorId 演员id
     */
    void remove(Long userId, Long actorId);
}

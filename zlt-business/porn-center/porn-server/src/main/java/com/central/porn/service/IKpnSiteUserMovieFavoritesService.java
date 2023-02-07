package com.central.porn.service;

import com.central.common.model.KpnSiteUserMovieFavorites;
import com.central.common.service.ISuperService;


public interface IKpnSiteUserMovieFavoritesService extends ISuperService<KpnSiteUserMovieFavorites> {

    /**
     * 查询收藏影片
     *
     * @param userId  会员id
     * @return
     */
    KpnSiteUserMovieFavorites getFavoritesByPage(Long userId);

    /**
     * 查询收藏影片
     *
     * @param userId  会员id
     * @param movieId 影片id
     * @return
     */
    KpnSiteUserMovieFavorites getUserMovieFavorites(Long userId, Long movieId);

    /**
     * 添加收藏
     *
     * @param userId  会员id
     * @param movieId 影片id
     */
    void add(Long userId, Long movieId);

    /**
     * 取消收藏
     *
     * @param userId  会员id
     * @param movieId 影片id
     */
    void remove(Long userId, Long movieId);

}

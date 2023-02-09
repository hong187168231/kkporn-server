package com.central.porn.service;

/**
 * 异步接口
 */
public interface IAsyncService {

    /**
     * 站点影片播放量-add
     *
     * @param sid     站点id
     * @param movieId 影片id
     */
    void addSiteMovieVv(Long sid, Long movieId);

    /**
     * 站点影片收藏量-加1
     *
     * @param sid     站点id
     * @param movieId 影片id
     */
    void addSiteMovieFavorites(Long sid, Long movieId);

    /**
     * 站点影片收藏量-减1
     *
     * @param sid     站点id
     * @param movieId 影片id
     */
    void removeSiteMovieFavorites(Long sid, Long movieId);

    /**
     * 会员收藏演员
     *
     * @param sid     站点id
     * @param actorId 演员id
     */
    void addSiteActorFavorites(Long sid, Long actorId);

    /**
     * 会员取消演员收藏
     *
     * @param sid     站点id
     * @param actorId 演员id
     */
    void removeSiteActorFavorites(Long sid, Long actorId);

}

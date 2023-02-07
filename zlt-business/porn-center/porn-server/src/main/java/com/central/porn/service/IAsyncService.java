package com.central.porn.service;

/**
 * 异步接口
 */
public interface IAsyncService {

    /**
     * 播放量-add
     *
     * @param sid     站点id
     * @param movieId 影片id
     */
    void addSiteMovieVv(Long sid, Long movieId);

    /**
     * 收藏量-加1
     *
     * @param sid     站点id
     * @param movieId 影片id
     */
    void addSiteMovieFavorites(Long sid, Long movieId);

    /**
     * 收藏量-减1
     *
     * @param sid     站点id
     * @param movieId 影片id
     */
    void removeSiteMovieFavorites(Long sid, Long movieId);
}

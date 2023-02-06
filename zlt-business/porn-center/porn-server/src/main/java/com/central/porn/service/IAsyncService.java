package com.central.porn.service;

/**
 * 异步接口
 */
public interface IAsyncService {

    /**
     * 播放量
     *
     * @param sid     站点id
     * @param movieId 影片id
     */
    void addSiteMovieVv(Long sid, Long movieId);

    /**
     * 收藏量
     *
     * @param sid     站点id
     * @param movieId 影片id
     */
    void addSiteMovieFavorites(Long sid, Long movieId);
}

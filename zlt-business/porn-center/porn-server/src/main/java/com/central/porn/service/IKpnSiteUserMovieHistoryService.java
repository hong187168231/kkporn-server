package com.central.porn.service;

import com.central.common.model.KpnSiteUserMovieFavorites;
import com.central.common.model.KpnSiteUserMovieHistory;
import com.central.common.service.ISuperService;

import java.util.List;


public interface IKpnSiteUserMovieHistoryService extends ISuperService<KpnSiteUserMovieHistory> {

    /**
     * 分页查询浏览影片
     *
     * @param userId   会员id
     * @param currPage 当前页数
     * @param pageSize 每页条数
     * @return
     */
    List<KpnSiteUserMovieHistory> getHistoryByPage(Long userId, Integer currPage, Integer pageSize);

    /**
     * 查询收藏影片
     *
     * @param userId  会员id
     * @param movieId 影片id
     * @return
     */
    KpnSiteUserMovieHistory getUserMovieHistory(Long userId, Long movieId);

    /**
     * 添加l浏览历史
     *
     * @param userId  会员id
     * @param movieId 影片id
     */
    void addUserMovieHistory(Long userId, Long movieId);

}

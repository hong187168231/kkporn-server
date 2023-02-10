package com.central.porn.service;

import com.central.common.model.KpnSiteMovie;
import com.central.common.service.ISuperService;
import com.central.porn.entity.vo.KpnMovieVo;
import com.central.porn.entity.vo.KpnSiteMovieBaseVo;

import java.util.List;


public interface IKpnSiteMovieService extends ISuperService<KpnSiteMovie> {

    /**
     * 获取站点影片列表基本信息
     *
     * @param sid      站点id
     * @param movieIds 影片id集合
     * @return
     */
    List<KpnSiteMovieBaseVo> getSiteMovieByIds(Long sid, List<Long> movieIds);

    /**
     * 获取影片详情
     *
     * @param sid     站点id
     * @param movieId 影片id
     * @return
     */
    KpnMovieVo getSiteMovieDetail(Long sid, Long movieId);

    /**
     * 获取站点影片信息
     *
     * @param sid     站点id
     * @param movieId 影片id
     * @return
     */
    KpnSiteMovie getSiteMovie(Long sid, Long movieId);

    /**
     * 增加站点影片播放量
     *
     * @param sid     站点id
     * @param movieId 影片id
     */
    Long addSiteMovieVv(Long sid, Long movieId);

    /**
     * 增加站点影片收藏量
     *
     * @param sid     站点id
     * @param userId  会员id
     * @param movieId 影片id
     */
    Long addSiteMovieFavorites(Long sid, Long userId, Long movieId);

    /**
     * 移除站点影片收藏量
     *
     * @param sid     站点id
     * @param movieId 影片id
     */
    Long removeSiteMovieFavorites(Long sid, Long userId, Long movieId);

    /**
     *
     * @param sid
     * @param actorId
     * @return
     */
    Long getSiteActorMovieNum(Long sid, Long actorId);
}

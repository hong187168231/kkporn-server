package com.central.porn.service;

import com.central.common.model.KpnSiteMovie;
import com.central.common.service.ISuperService;
import com.central.porn.entity.co.MovieSearchParamCo;
import com.central.porn.entity.vo.KpnMovieVo;
import com.central.porn.entity.vo.KpnSiteMovieBaseVo;

import java.util.List;
import java.util.Map;


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
     * 获取站点演员数量
     *
     * @param sid     站点id
     * @param actorId 演员id
     * @return
     */
    Long getSiteActorMovieNum(Long sid, Long actorId);


    /**
     * 分页查询站点演员影片
     *
     * @param sid       站点id
     * @param actorId   演员id
     * @param sortType  排序类型 HOT,LATEST,TIME
     * @param sortOrder 排序顺序 0正序,1倒序
     * @param actorId   演员id
     * @param currPage  当前页数
     * @param pageSize  每页条数
     * @return
     */
    List<KpnSiteMovieBaseVo> getSiteMovieByActor(Long sid, Long actorId, String sortType, Integer sortOrder, Integer currPage, Integer pageSize);

    /**
     * 搜索站点影片
     *
     * @param sid         站点id
     * @param searchParam 查询参数
     * @param sortType    排序字段
     * @param sortOrder   排序顺序
     * @param currPage    当前页
     * @param pageSize    每页条数
     * @return
     */
    List<KpnSiteMovieBaseVo> searchSiteMovie(Long sid, MovieSearchParamCo searchParam, String sortType, Integer sortOrder, Integer currPage, Integer pageSize);

    /**
     * 月排行榜
     *
     * @param sid 站点id
     * @return
     */
    List<KpnSiteMovieBaseVo> searchSiteMovieMonth(Long sid);
}

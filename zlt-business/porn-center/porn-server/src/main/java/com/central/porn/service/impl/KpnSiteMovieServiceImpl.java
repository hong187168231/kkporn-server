package com.central.porn.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.central.common.constant.PornConstants;
import com.central.common.model.KpnMovie;
import com.central.common.model.KpnSiteMovie;
import com.central.common.redis.template.RedisRepository;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.mapper.KpnSiteMovieMapper;
import com.central.porn.service.IAsyncService;
import com.central.porn.service.IKpnMovieService;
import com.central.porn.service.IKpnSiteMovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class KpnSiteMovieServiceImpl extends SuperServiceImpl<KpnSiteMovieMapper, KpnSiteMovie> implements IKpnSiteMovieService {

    @Autowired
    private IKpnMovieService movieService;

    @Autowired
    private IAsyncService asyncService;

    @Override
    public Long addSiteMovieVv(Long sid, Long movieId) {
        String siteMovieVvKey = StrUtil.format(PornConstants.RedisKey.KPN_SITE_MOVIE_VV_KEY, sid, movieId);
        Object siteMovieVvObj = RedisRepository.get(siteMovieVvKey);
        if (ObjectUtil.isEmpty(siteMovieVvObj)) {
            Long vv = this.lambdaQuery().eq(KpnSiteMovie::getSiteId, sid)
                    .eq(KpnSiteMovie::getMovieId, movieId)
                    .one().getVv();

            //1天过期
            RedisRepository.setExpire(siteMovieVvKey, vv, PornConstants.RedisKey.EXPIRE_TIME_1_DAYS, TimeUnit.SECONDS);
        }

        //异步增加站点影片播放量
        asyncService.addSiteMovieVv(sid, movieId);

        // 异步增加影片总播放量
        movieService.addMovieVv(movieId);

        return RedisRepository.incr(siteMovieVvKey);
    }

    @Override
    public Long addSiteMovieFavorites(Long sid, Long movieId) {
        String siteMovieFavoritesKey = StrUtil.format(PornConstants.RedisKey.KPN_SITE_MOVIE_FAVORITES_KEY, sid, movieId);
        Object siteMovieFavoritesObj = RedisRepository.get(siteMovieFavoritesKey);
        if (ObjectUtil.isEmpty(siteMovieFavoritesObj)) {
            Long favorites = this.lambdaQuery().eq(KpnSiteMovie::getSiteId, sid)
                    .eq(KpnSiteMovie::getMovieId, movieId)
                    .one().getFavorites();

            //1天过期
            RedisRepository.setExpire(siteMovieFavoritesKey, favorites, PornConstants.RedisKey.EXPIRE_TIME_1_DAYS, TimeUnit.SECONDS);
        }

        //异步增加站点影片播放量
        asyncService.addSiteMovieFavorites(sid, movieId);

        // 异步增加影片总播放量
        movieService.addMovieFavorites(movieId);

        return RedisRepository.incr(siteMovieFavoritesKey);
    }
}

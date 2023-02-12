package com.central.porn.service.impl;

import cn.hutool.core.date.DateUtil;
import com.central.common.model.KpnSiteActor;
import com.central.common.model.KpnSiteMovie;
import com.central.porn.service.IAsyncService;
import com.central.porn.service.IKpnSiteActorService;
import com.central.porn.service.IKpnSiteMovieService;
import com.central.porn.service.IRptSiteMovieDateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class AsyncServiceImpl implements IAsyncService {

    @Autowired
    @Lazy
    private IKpnSiteMovieService siteMovieService;

    @Autowired
    @Lazy
    private IKpnSiteActorService siteActorService;

    @Autowired
    private IRptSiteMovieDateService rptSiteMovieDateService;

    @Async
    @Override
    public void addSiteMovieVv(Long sid, Long movieId) {
        siteMovieService.lambdaUpdate()
                .eq(KpnSiteMovie::getSiteId, sid)
                .eq(KpnSiteMovie::getMovieId, movieId)
                .setSql(" `vv` = `vv` + 1")
                .update();

        rptSiteMovieDateService.saveRptSiteMovieDateVv(sid, movieId, DateUtil.formatDate(new Date()));

//        rptSiteMovieDateService.lambdaUpdate()
//                .eq(RptSiteMovieDate::getSiteId, sid)
//                .eq(RptSiteMovieDate::getMovieId, movieId)
//                .eq(RptSiteMovieDate::getDate, DateUtil.formatDate(new Date()))
//                .setSql(" `vv` = `vv` + 1")
//                .update();
    }

    @Async
    @Override
    public void addSiteMovieFavorites(Long sid, Long movieId) {
        siteMovieService.lambdaUpdate()
                .eq(KpnSiteMovie::getSiteId, sid)
                .eq(KpnSiteMovie::getMovieId, movieId)
                .setSql(" `favorites` = `favorites` + 1")
                .update();
    }

    @Async
    @Override
    public void removeSiteMovieFavorites(Long sid, Long movieId) {
        siteMovieService.lambdaUpdate()
                .eq(KpnSiteMovie::getSiteId, sid)
                .eq(KpnSiteMovie::getMovieId, movieId)
                .setSql(" `favorites` = `favorites` - 1")
                .update();
    }

    @Async
    @Override
    public void addSiteActorFavorites(Long sid, Long actorId) {
        siteActorService.lambdaUpdate()
                .eq(KpnSiteActor::getSiteId, sid)
                .eq(KpnSiteActor::getActorId, actorId)
                .setSql(" `favorites` = `favorites` + 1")
                .update();
    }

    @Async
    @Override
    public void removeSiteActorFavorites(Long sid, Long actorId) {
        siteActorService.lambdaUpdate()
                .eq(KpnSiteActor::getSiteId, sid)
                .eq(KpnSiteActor::getActorId, actorId)
                .setSql(" `favorites` = `favorites` - 1")
                .update();
    }
}

package com.central.porn.service.impl;

import com.central.common.model.KpnSiteActor;
import com.central.common.model.KpnSiteMovie;
import com.central.common.model.KpnSiteUserActorFavorites;
import com.central.porn.service.IAsyncService;
import com.central.porn.service.IKpnSiteActorService;
import com.central.porn.service.IKpnSiteMovieService;
import com.central.porn.service.IKpnSiteUserActorFavoritesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AsyncServiceImpl implements IAsyncService {

    @Autowired
    @Lazy
    private IKpnSiteMovieService siteMovieService;

    @Autowired
    @Lazy
    private IKpnSiteActorService siteActorService;

    @Async
    @Override
    public void addSiteMovieVv(Long sid, Long movieId) {
        siteMovieService.lambdaUpdate()
                .eq(KpnSiteMovie::getSiteId, sid)
                .eq(KpnSiteMovie::getMovieId, movieId)
                .setSql(" `vv` = `vv` + 1")
                .update();
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

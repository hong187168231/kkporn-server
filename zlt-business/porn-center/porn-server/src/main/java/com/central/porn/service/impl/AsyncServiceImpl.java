package com.central.porn.service.impl;

import com.central.common.model.KpnSiteMovie;
import com.central.porn.service.IAsyncService;
import com.central.porn.service.IKpnSiteMovieService;
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

    @Async
    @Override
    public void addSiteMovieVv(Long sid, Long movieId) {
        siteMovieService.lambdaUpdate()
                .eq(KpnSiteMovie::getSiteId, sid)
                .eq(KpnSiteMovie::getMovieId, movieId)
                .setSql(" `vv` = `vv` + 1")
                .update();
    }
}

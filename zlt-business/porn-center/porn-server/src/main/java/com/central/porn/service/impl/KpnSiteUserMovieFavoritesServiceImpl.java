package com.central.porn.service.impl;

import com.central.common.model.KpnSiteUserMovieFavorites;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.mapper.KpnSiteUserMovieFavoritesMapper;
import com.central.porn.service.IKpnSiteUserMovieFavoritesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KpnSiteUserMovieFavoritesServiceImpl extends SuperServiceImpl<KpnSiteUserMovieFavoritesMapper, KpnSiteUserMovieFavorites> implements IKpnSiteUserMovieFavoritesService {

    @Override
    public KpnSiteUserMovieFavorites getFavoritesByPage(Long userId) {
        return null;
    }

    @Override
    public KpnSiteUserMovieFavorites getUserMovieFavorites(Long userId, Long movieId) {
        return this.lambdaQuery()
                .eq(KpnSiteUserMovieFavorites::getUserId, userId)
                .eq(KpnSiteUserMovieFavorites::getMovieId, movieId)
                .one();
    }

    @Async
    @Override
    public void add(Long userId, Long movieId) {
        KpnSiteUserMovieFavorites userMovieFavorites = KpnSiteUserMovieFavorites.builder().userId(userId).movieId(movieId).build();
        save(userMovieFavorites);
    }

    @Async
    @Override
    public void remove(Long userId, Long movieId) {
        this.lambdaUpdate()
                .eq(KpnSiteUserMovieFavorites::getUserId, userId)
                .eq(KpnSiteUserMovieFavorites::getMovieId, movieId)
                .remove();
    }
}

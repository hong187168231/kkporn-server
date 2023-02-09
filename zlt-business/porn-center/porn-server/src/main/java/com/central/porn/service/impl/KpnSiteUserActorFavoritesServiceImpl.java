package com.central.porn.service.impl;

import com.central.common.model.KpnSiteUserActorFavorites;
import com.central.common.model.KpnSiteUserMovieFavorites;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.mapper.KpnSiteUserActorFavoritesMapper;
import com.central.porn.service.IKpnSiteUserActorFavoritesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KpnSiteUserActorFavoritesServiceImpl extends SuperServiceImpl<KpnSiteUserActorFavoritesMapper, KpnSiteUserActorFavorites> implements IKpnSiteUserActorFavoritesService {

    @Override
    public KpnSiteUserActorFavorites getUserActorFavorites(Long userId, Long actorId) {
        return this.lambdaQuery()
                .eq(KpnSiteUserActorFavorites::getUserId, userId)
                .eq(KpnSiteUserActorFavorites::getActorId, actorId)
                .one();
    }

    @Async
    @Override
    public void add(Long userId, Long actorId) {
        KpnSiteUserActorFavorites userActorFavorites = KpnSiteUserActorFavorites.builder().userId(userId).actorId(actorId).build();
        save(userActorFavorites);
    }

    @Async
    @Override
    public void remove(Long userId, Long actorId) {
        this.lambdaUpdate()
                .eq(KpnSiteUserActorFavorites::getUserId, userId)
                .eq(KpnSiteUserActorFavorites::getActorId, actorId)
                .remove();
    }
}

























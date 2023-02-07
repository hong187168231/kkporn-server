package com.central.porn.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.central.common.model.KpnSiteUserMovieFavorites;
import com.central.common.model.KpnSiteUserMovieHistory;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.mapper.KpnSiteUserMovieFavoritesMapper;
import com.central.porn.mapper.KpnSiteUserMovieHistoryMapper;
import com.central.porn.service.IKpnSiteUserMovieFavoritesService;
import com.central.porn.service.IKpnSiteUserMovieHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class KpnSiteUserMovieHistoryServiceImpl extends SuperServiceImpl<KpnSiteUserMovieHistoryMapper, KpnSiteUserMovieHistory> implements IKpnSiteUserMovieHistoryService {


    @Override
    public List<KpnSiteUserMovieHistory> getHistoryByPage(Long userId, Integer currPage, Integer pageSize) {
        return null;
    }

    @Override
    public KpnSiteUserMovieHistory getUserMovieHistory(Long userId, Long movieId) {
        return this.lambdaQuery()
                .eq(KpnSiteUserMovieHistory::getUserId, userId)
                .eq(KpnSiteUserMovieHistory::getMovieId, movieId)
                .one();
    }

    @Async
    @Override
    public void addUserMovieHistory(Long userId, Long movieId) {
        KpnSiteUserMovieHistory userMovieHistory = getUserMovieHistory(userId, movieId);
        if (ObjectUtil.isNotNull(userMovieHistory)) {
            this.lambdaUpdate()
                    .set(KpnSiteUserMovieHistory::getCreateTime, new Date())
                    .eq(KpnSiteUserMovieHistory::getUserId, userId)
                    .eq(KpnSiteUserMovieHistory::getMovieId, movieId)
                    .update();
        } else {
            KpnSiteUserMovieHistory siteUserMovieHistory = KpnSiteUserMovieHistory.builder().userId(userId).movieId(movieId).build();
            save(siteUserMovieHistory);
        }

    }


}

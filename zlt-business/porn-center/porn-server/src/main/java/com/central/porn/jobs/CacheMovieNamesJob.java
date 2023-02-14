package com.central.porn.jobs;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.central.common.constant.PornConstants;
import com.central.common.model.KpnSite;
import com.central.common.model.KpnSiteMovie;
import com.central.common.model.enums.SiteMovieStatusEnum;
import com.central.common.redis.template.RedisRepository;
import com.central.porn.service.IKpnSiteMovieService;
import com.central.porn.service.IKpnSiteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CacheMovieNamesJob implements CommandLineRunner {

    @Autowired
    private IKpnSiteService kpnSiteService;

    @Autowired
    private IKpnSiteMovieService siteMovieService;

    @Scheduled(initialDelay = 10 * 1000, fixedDelay = 60 * 1000)
    public void cache() {
        log.info("CacheMovieNamesJob is running ....");

        //为空时加载
        if (CollectionUtil.isEmpty(PornConstants.LocalCache.LOCAL_MAP_MOVIE_NAME)) {
            cacheData();
        }

        List<KpnSite> kpnSites = kpnSiteService.lambdaQuery().eq(KpnSite::getStatus, true).list();
        for (KpnSite kpnSite : kpnSites) {
            Long sid = kpnSite.getId();

            String redisFlagKey = StrUtil.format(PornConstants.RedisKey.SITE_MOVIE_CHANGE_FLAG, sid);
            Integer oper = (Integer) RedisRepository.get(redisFlagKey);
            if (oper != null && oper == PornConstants.Numeric.OPEN) {
                cacheData();
                reCacheSiteData(sid);
                log.info("sid:{},数据同步完成!", sid);
            }
            RedisRepository.set(redisFlagKey, PornConstants.Numeric.CLOSE);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        List<KpnSite> kpnSites = kpnSiteService.lambdaQuery().eq(KpnSite::getStatus, true).list();
        for (KpnSite kpnSite : kpnSites) {
            String redisFlagKey = StrUtil.format(PornConstants.RedisKey.SITE_MOVIE_CHANGE_FLAG, kpnSite.getId());
            RedisRepository.set(redisFlagKey, 1);
        }
    }

    private void cacheData() {
        PornConstants.LocalCache.LOCAL_MAP_MOVIE_NAME.clear();
        List<KpnSiteMovie> kpnSiteMovies = siteMovieService.lambdaQuery()
                .eq(KpnSiteMovie::getStatus, SiteMovieStatusEnum.ON_SHELF.getStatus())
                .list();

        Map<Long, String[]> collect = kpnSiteMovies.stream().collect(Collectors.toMap(KpnSiteMovie::getMovieId,
                kpnSiteMovie -> new String[]{kpnSiteMovie.getNameZh(), kpnSiteMovie.getNameEn(), kpnSiteMovie.getNameKh()}, (s1, s2) -> s2));
        PornConstants.LocalCache.LOCAL_MAP_MOVIE_NAME.putAll(collect);

        //暗示
        System.gc();
    }

    private void reCacheSiteData(Long sid) {
        PornConstants.LocalCache.LOCAL_MAP_SITE_MOVIE_IDS.remove(sid);
        List<KpnSiteMovie> kpnSiteMovies = siteMovieService.lambdaQuery()
                .eq(KpnSiteMovie::getSiteId, sid)
                .eq(KpnSiteMovie::getStatus, SiteMovieStatusEnum.ON_SHELF.getStatus())
                .orderByDesc(KpnSiteMovie::getVv).list();

        List<Long> movieIds = kpnSiteMovies.stream().map(KpnSiteMovie::getMovieId).collect(Collectors.toList());
        PornConstants.LocalCache.LOCAL_MAP_SITE_MOVIE_IDS.put(sid, movieIds);
    }


}

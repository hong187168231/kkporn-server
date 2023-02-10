package com.central.porn.jobs;

import com.central.common.model.KpnSiteMovie;
import com.central.porn.service.IKpnSiteMovieService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class ActorMovieVvRpt implements SimpleJob {

    @Autowired
    private IKpnSiteMovieService siteMovieService;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("ActorMovieVvRpt -> params:{}, time:{}", shardingContext.getJobParameter(), LocalDateTime.now());
//        List<KpnSiteMovie> kpnSiteMovies = siteMovieService.lambdaQuery()
//                .select(KpnSiteMovie::getSiteId, KpnSiteMovie::getActorId)
//                .groupBy(KpnSiteMovie::getSiteId, KpnSiteMovie::getActorId)
//                .list();
//
//        for (KpnSiteMovie kpnSiteMovie : kpnSiteMovies) {
//            Long siteId = kpnSiteMovie.getSiteId();
//            Long actorId = kpnSiteMovie.getActorId();
//
//        }
    }
}

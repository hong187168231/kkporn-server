package com.central.porn.jobs;

import cn.hutool.core.util.StrUtil;
import com.central.common.constant.PornConstants;
import com.central.common.model.KpnSite;
import com.central.common.redis.template.RedisRepository;
import com.central.porn.service.IKpnSiteMovieService;
import com.central.porn.service.IKpnSiteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 刷新视频播放量
 */
@Slf4j
@Component
public class RefreshSiteMovieVvRptJob implements SimpleJob {

    @Autowired
    private IKpnSiteMovieService siteMovieService;

    @Autowired
    private IKpnSiteService siteService;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("RefreshSiteMovieVvRptJob -> params:{}, time:{}", shardingContext.getJobParameter(), LocalDateTime.now());
        try {
            List<KpnSite> kpnSites = siteService.getList();
            for (KpnSite kpnSite : kpnSites) {
                Long sid = kpnSite.getId();
                List<Long> movieIds = siteMovieService.getSiteMovieIds(sid);
                for (Long movieId : movieIds) {
                    String redisKey = StrUtil.format(PornConstants.RedisKey.KPN_MOVIEID_VO_KEY, movieId);
                    RedisRepository.delete(redisKey);
                    siteMovieService.getSiteMovieByIds(sid, Collections.singletonList(movieId), false);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}

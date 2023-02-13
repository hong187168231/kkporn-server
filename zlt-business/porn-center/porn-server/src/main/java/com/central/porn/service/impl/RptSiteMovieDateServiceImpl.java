package com.central.porn.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.central.common.constant.PornConstants;
import com.central.common.model.KpnMovie;
import com.central.common.model.RptSiteMovieDate;
import com.central.common.model.enums.SiteMovieStatusEnum;
import com.central.common.redis.template.RedisRepository;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.entity.co.MovieSearchParamCo;
import com.central.porn.entity.vo.KpnSiteMovieBaseVo;
import com.central.porn.enums.KpnMovieSortTypeEnum;
import com.central.porn.enums.KpnSortOrderEnum;
import com.central.porn.mapper.KpnMovieMapper;
import com.central.porn.mapper.RptSiteMovieDateMapper;
import com.central.porn.service.IKpnMovieService;
import com.central.porn.service.IKpnSiteMovieService;
import com.central.porn.service.IRptSiteMovieDateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RptSiteMovieDateServiceImpl extends SuperServiceImpl<RptSiteMovieDateMapper, RptSiteMovieDate> implements IRptSiteMovieDateService {

    @Autowired
    @Lazy
    private IKpnSiteMovieService siteMovieService;

    @Override
    public void saveRptSiteMovieDateVv(Long sid, Long movieId, String date) {
        this.baseMapper.saveRptSiteMovieDateVv(sid, movieId, date);
    }

    @Override
    public List<KpnSiteMovieBaseVo> searchSiteMovieMonth(Long sid) {
        String redisKey = StrUtil.format(PornConstants.RedisKey.KPN_SITE_MONTH_MOVIE_KEY, sid);
        List<KpnSiteMovieBaseVo> siteMonthMovies = (ArrayList<KpnSiteMovieBaseVo>) RedisRepository.get(redisKey);
        if (CollectionUtil.isEmpty(siteMonthMovies)) {
            String endDate = DateUtil.formatDate(new Date());
            String startDate = DateUtil.formatDate(DateUtil.offsetDay(new Date(), -30));
            log.info("startDate:{}, endDate:{}", startDate, endDate);

            List<RptSiteMovieDate> rptSiteMovieDates = this.baseMapper.searchSiteMovieMonth(sid, startDate, endDate);
            List<Long> movieIds = rptSiteMovieDates.stream().map(RptSiteMovieDate::getMovieId).collect(Collectors.toList());
            siteMonthMovies = siteMovieService.getSiteMovieByIds(sid, movieIds);
            if (siteMonthMovies.size() < 10) {
                MovieSearchParamCo movieSearchParam = MovieSearchParamCo.builder().build();
                List<KpnSiteMovieBaseVo> kpnSiteMovieBaseVos = siteMovieService.searchSiteMovie(sid, movieSearchParam, KpnMovieSortTypeEnum.HOT.getType(), KpnSortOrderEnum.DESC.getCode(), 1, 10 - siteMonthMovies.size());
                siteMonthMovies.addAll(kpnSiteMovieBaseVos);
            }

            if (CollectionUtil.isNotEmpty(siteMonthMovies)) {
                RedisRepository.setExpire(redisKey, siteMonthMovies, PornConstants.RedisKey.EXPIRE_TIME_30_DAYS);
            }
        }
        return siteMonthMovies;
    }
}
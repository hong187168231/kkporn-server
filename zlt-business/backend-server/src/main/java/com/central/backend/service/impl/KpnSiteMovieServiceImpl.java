package com.central.backend.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.backend.controller.movie.KpnSiteMovieVo;
import com.central.backend.controller.movie.QueryMovieCo;
import com.central.backend.mapper.KpnSiteMovieMapper;
import com.central.backend.model.dto.KpnSiteMoviePayTypeDto;
import com.central.backend.model.dto.KpnSiteMovieStatusDto;
import com.central.backend.service.IKpnMovieTagService;
import com.central.backend.service.IKpnSiteMovieService;
import com.central.backend.vo.MovieVo;
import com.central.common.KpnMovieTag;
import com.central.common.model.KpnSiteMovie;
import com.central.common.model.PageResult;
import com.central.common.model.SysUser;
import com.central.common.model.enums.KpnMovieCountryEnum;
import com.central.common.model.enums.SiteMovieStatusEnum;
import com.central.common.service.impl.SuperServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 站点影片
 */
@Slf4j
@Service
public class KpnSiteMovieServiceImpl extends SuperServiceImpl<KpnSiteMovieMapper, KpnSiteMovie> implements IKpnSiteMovieService {

    @Autowired
    private IKpnMovieTagService movieTagService;

    @Override
    public PageResult<KpnSiteMovieVo> list(QueryMovieCo queryMovieCo) {
        Page<KpnSiteMovie> page = new Page<>(queryMovieCo.getPage(), queryMovieCo.getLimit());
        LambdaQueryWrapper<KpnSiteMovie> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KpnSiteMovie::getSiteId, queryMovieCo.getSiteId());
        //更新时间
        if (StrUtil.isNotBlank(queryMovieCo.getStartTime())) {
            wrapper.ge(KpnSiteMovie::getUpdateTime, queryMovieCo.getStartTime());
        }
        if (StrUtil.isNotBlank(queryMovieCo.getEndTime())) {
            wrapper.le(KpnSiteMovie::getUpdateTime, queryMovieCo.getEndTime());
        }
        //站点影片状态
        if (!SiteMovieStatusEnum.isAll(queryMovieCo.getCountry())) {
            wrapper.eq(KpnSiteMovie::getStatus, queryMovieCo.getStatus());
        }
        //国家
        if (!KpnMovieCountryEnum.isAll(queryMovieCo.getCountry())) {
            wrapper.eq(KpnSiteMovie::getCountry, queryMovieCo.getCountry());
        }
        //标签分类
        if (ObjectUtil.isNotEmpty(queryMovieCo.getTagCategoryId())) {
            LambdaQueryChainWrapper<KpnMovieTag> movieTagWrapper = movieTagService.lambdaQuery();
            movieTagWrapper.select(KpnMovieTag::getMovieId);
            movieTagWrapper.eq(KpnMovieTag::getTagCategoryId, queryMovieCo.getTagCategoryId());
            if (ObjectUtil.isNotEmpty(queryMovieCo.getTagId())) {
                movieTagWrapper.eq(KpnMovieTag::getTagId, queryMovieCo.getTagId());
            }
            List<Long> movieIds = movieTagWrapper.list().stream().map(KpnMovieTag::getMovieId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(movieIds)) {
                wrapper.in(KpnSiteMovie::getMovieId, movieIds);
            }
        }


        Page<KpnSiteMovie> pageResult = baseMapper.selectPage(page, wrapper);

        List<KpnSiteMovieVo> siteMovieVos = new ArrayList<>();
        List<KpnSiteMovie> records = pageResult.getRecords();
        for (KpnSiteMovie siteMovie : records) {
            KpnSiteMovieVo siteMovieVo = new KpnSiteMovieVo();
            BeanUtils.copyProperties(siteMovie, siteMovieVo);
            siteMovieVos.add(siteMovieVo);
        }

        return PageResult.<KpnSiteMovieVo>builder().data(siteMovieVos).count(page.getTotal()).build();
    }

    @Override
    public void updateBatchStatusById(List<KpnSiteMovieStatusDto> list,SysUser user){
        List<KpnSiteMovie> movieList = new ArrayList<>();
        for (KpnSiteMovieStatusDto dto : list){
            KpnSiteMovie movie = new KpnSiteMovie();
            BeanUtils.copyProperties(dto, movie);
            movie.setUpdateBy(null!=user?user.getUsername():"");
            movie.setUpdateTime(new Date());
            movieList.add(movie);
        }
        baseMapper.updateBatchStatusById(movieList);
    }

    @Override
    public void updateBatchPayTypeById(List<KpnSiteMoviePayTypeDto> list, SysUser user){
        List<KpnSiteMovie> movieList = new ArrayList<>();
        for (KpnSiteMoviePayTypeDto dto : list){
            KpnSiteMovie movie = new KpnSiteMovie();
            BeanUtils.copyProperties(dto, movie);
            movie.setUpdateBy(null!=user?user.getUsername():"");
            movie.setUpdateTime(new Date());
            movieList.add(movie);
        }
        baseMapper.updateBatchPayTypeById(movieList);
    }

    @Override
    public PageResult<MovieVo> findMovieList(Map<String, Object> params) {
        Page<MovieVo> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
        List<MovieVo> list  =  baseMapper.findMovieList(page, params);
        return PageResult.<MovieVo>builder().data(list).count(page.getTotal()).build();
    }
}

package com.central.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.backend.controller.movie.KpnSiteMovieVO;
import com.central.backend.controller.movie.QueryMovieCo;
import com.central.backend.mapper.KpnSiteMovieMapper;
import com.central.backend.model.dto.KpnSiteMoviePayTypeDto;
import com.central.backend.model.dto.KpnSiteMovieStatusDto;
import com.central.backend.service.IKpnSiteMovieService;
import com.central.backend.vo.MovieVo;
import com.central.common.model.KpnSiteMovie;
import com.central.common.model.PageResult;
import com.central.common.model.SysUser;
import com.central.common.service.impl.SuperServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 站点影片
 */
@Slf4j
@Service
public class KpnSiteMovieServiceImpl extends SuperServiceImpl<KpnSiteMovieMapper, KpnSiteMovie> implements IKpnSiteMovieService {
    @Override
    public PageResult<KpnSiteMovieVO> list(QueryMovieCo queryMovieCo, Long siteId) {
        Page<KpnSiteMovie> page = new Page<>(queryMovieCo.getPage(), queryMovieCo.getLimit());
        LambdaQueryWrapper<KpnSiteMovie> wrapper = new LambdaQueryWrapper<>();
        Page<KpnSiteMovie> pageResult = baseMapper.selectPage(page, wrapper);

        List<KpnSiteMovie> records = pageResult.getRecords();

        return PageResult.<KpnSiteMovieVO>builder().data(new ArrayList<>()).count(page.getTotal()).build();
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

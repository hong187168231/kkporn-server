package com.central.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.backend.mapper.KpnSiteTopicMovieMapper;
import com.central.backend.model.vo.KpnSiteMovieVO;
import com.central.backend.model.vo.KpnTagCategoryVO;
import com.central.backend.service.IKpnSiteTopicMovieService;
import com.central.backend.vo.SiteMovieListVo;
import com.central.common.model.KpnSiteSuggestion;
import com.central.common.model.KpnSiteTopicMovie;
import com.central.common.model.PageResult;
import com.central.common.service.impl.SuperServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class KpnSiteTopicMovieServiceImpl extends SuperServiceImpl<KpnSiteTopicMovieMapper, KpnSiteTopicMovie> implements IKpnSiteTopicMovieService {


    @Override
    public Boolean saveOrUpdateTopicMovie(List<KpnSiteTopicMovie> list) {
        return super.saveOrUpdateBatch(list);
    }

    @Override
    public List<KpnSiteTopicMovie> findTopicMovieTopicIdList(Long topicId) {
        LambdaQueryWrapper<KpnSiteTopicMovie> wrapper=new LambdaQueryWrapper<>();
        if (topicId!=null){
            wrapper.eq(KpnSiteTopicMovie::getTopicId,topicId);
        }
        return  baseMapper.selectList(wrapper);
    }

    @Override
    public Boolean deleteTopicId(List<Long> movieIds) {
        int i = baseMapper.deleteBatchIds(movieIds);
        return i > 0 ? true : false;
    }

    @Override
    public Boolean deleteId(Long id) {
        int i = baseMapper.deleteById(id);
        return i > 0 ? true : false;
    }


    @Override
    public PageResult<SiteMovieListVo> findSiteMovieList(Map<String, Object> params) {
        Page<SiteMovieListVo> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
        List<SiteMovieListVo> list  =  baseMapper.findSiteMovieList(page,params);
        return PageResult.<SiteMovieListVo>builder().data(list).count(page.getTotal()).build();
    }
}
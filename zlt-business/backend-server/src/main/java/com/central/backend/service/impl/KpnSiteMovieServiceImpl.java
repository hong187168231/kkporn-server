package com.central.backend.service.impl;

import com.central.backend.mapper.KpnSiteMovieMapper;
import com.central.backend.model.dto.KpnSiteMovieStatusDto;
import com.central.backend.model.vo.KpnSiteMovieVO;
import com.central.backend.service.IKpnSiteMovieService;
import com.central.backend.vo.MovieVo;
import com.central.common.model.KpnSiteMovie;
import com.central.common.model.SysUser;
import org.springframework.stereotype.Service;
import com.central.common.model.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.common.service.impl.SuperServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import lombok.extern.slf4j.Slf4j;


/**
 * 站点影片
 *
 * @author yixiu
 * @date 2023-02-20 17:00:56
 */
@Slf4j
@Service
public class KpnSiteMovieServiceImpl extends SuperServiceImpl<KpnSiteMovieMapper, KpnSiteMovie> implements IKpnSiteMovieService {
    /**
     * 列表
     * @param params
     * @return
     */
    @Override
    public PageResult<KpnSiteMovieVO> findList(Map<String, Object> params, SysUser user){
        if(null!=user && null!=user.getSiteId() && user.getSiteId()!=0){
            params.put("siteId",user.getSiteId());
        }
        Page<KpnSiteMovieVO> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
        List<KpnSiteMovieVO> list  =  baseMapper.findList(page, params);
        return PageResult.<KpnSiteMovieVO>builder().data(list).count(page.getTotal()).build();
    }
    @Override
    public void updateBatchStatusById(List<KpnSiteMovieStatusDto> list,SysUser user){
        for (KpnSiteMovieStatusDto dto : list){
            dto.setUpdateBy(null!=user?user.getUsername():dto.getUpdateBy());
            dto.setUpdateTime(new Date());
        }
        baseMapper.updateBatchStatusById(list);
    }

    @Override
    public void updateBatchPayTypeById(List<KpnSiteMovieStatusDto> list,SysUser user){
        for (KpnSiteMovieStatusDto dto : list){
            dto.setUpdateBy(null!=user?user.getUsername():dto.getUpdateBy());
            dto.setUpdateTime(new Date());
        }
        baseMapper.updateBatchPayTypeById(list);
    }

    @Override
    public PageResult<MovieVo> findMovieList(Map<String, Object> params) {
        Page<MovieVo> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
        List<MovieVo> list  =  baseMapper.findMovieList(page, params);
        return PageResult.<MovieVo>builder().data(list).count(page.getTotal()).build();
    }
}

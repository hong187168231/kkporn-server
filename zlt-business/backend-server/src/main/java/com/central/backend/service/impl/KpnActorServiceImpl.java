package com.central.backend.service.impl;

import com.central.backend.mapper.KpnActorMapper;
import com.central.backend.model.vo.KpnActorVO;
import com.central.backend.service.IKpnActorService;
import com.central.backend.service.IKpnMovieService;
import com.central.common.model.*;
import com.central.common.service.impl.SuperServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import lombok.extern.slf4j.Slf4j;


/**
 * 演员列表
 *
 * @author yixiu
 * @date 2023-02-03 16:31:09
 */
@Slf4j
@Service
public class KpnActorServiceImpl extends SuperServiceImpl<KpnActorMapper, KpnActor> implements IKpnActorService {
    @Autowired
    private IKpnMovieService iKpnMovieService;
    /**
     * 列表
     * @param params
     * @return
     */
    @Override
    public PageResult<KpnActorVO> findList(Map<String, Object> params, SysUser user){
        if(null!=user && null!=user.getSiteId() && user.getSiteId()!=0){//
            params.put("siteId",user.getSiteId());
        }
        Page<KpnActorVO> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
        List<KpnActorVO> list  =  baseMapper.findList(page, params);
        return PageResult.<KpnActorVO>builder().data(list).count(page.getTotal()).build();
    }
    @Override
    public Result deleteKpnActor(Long id){
        Map<String, Object> params = new HashMap<>();
        params.put("actorId",id);//演员ID
        List<KpnMovie>  kpnMovieList = iKpnMovieService.getKpnMovie(params);
        if(null!=kpnMovieList && kpnMovieList.size()>0) {
            return Result.failed("该演员名下有关联影片，禁止删除");
        }
        this.removeById(id);
        return Result.succeed("删除成功");
    }
}

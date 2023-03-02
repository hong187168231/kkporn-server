package com.central.backend.service.impl;

import com.central.backend.mapper.KpnTagMapper;
import com.central.backend.model.vo.KpnTagVO;
import com.central.backend.service.IKpnMovieTagService;
import com.central.backend.service.IKpnTagService;
import com.central.backend.vo.CategoryVo;
import com.central.backend.vo.KpnTagVo;
import com.central.common.KpnMovieTag;
import com.central.common.model.KpnTag;
import com.central.common.model.Result;
import com.central.common.model.SysUser;
import com.central.common.service.impl.SuperServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.central.common.model.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import lombok.extern.slf4j.Slf4j;


/**
 * 影片标签
 *
 * @author yixiu
 * @date 2023-02-03 16:32:41
 */
@Slf4j
@Service
public class KpnTagServiceImpl extends SuperServiceImpl<KpnTagMapper, KpnTag> implements IKpnTagService {
    @Autowired
    private IKpnMovieTagService iKpnMovieTagService;
    /**
     * 列表
     *
     * @param params
     * @return
     */
    @Override
    public PageResult<KpnTagVO> findList(Map<String, Object> params, SysUser user){
        if(null!=user && null!=user.getSiteId() && user.getSiteId()!=0){//
            params.put("siteId",user.getSiteId());
        }
        Page<KpnTagVO> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
        List<KpnTagVO> list  =  baseMapper.findList(page, params);
        return PageResult.<KpnTagVO>builder().data(list).count(page.getTotal()).build();
    }

    @Override
    public List<KpnTagVo> findTagList(Map<String, Object> params) {
        return baseMapper.findTagList(params);
    }

    @Override
    public List<CategoryVo> findTagCategoryList() {
        return baseMapper.findTagCategoryList();
    }
    @Override
    public Result removeKpnTag(Long id){
        Map<String, Object> params = new HashMap<>();
        params.put("tagId",id);//标签ID
        List<KpnMovieTag> kpnMovieTagList = iKpnMovieTagService.getKpnMovieTag(params);
        if(null!=kpnMovieTagList && kpnMovieTagList.size()>0) {
            return Result.failed("当前标签有关联影片，不可删除");
        }
        this.removeById(id);
        return Result.succeed("删除成功");
    }
}

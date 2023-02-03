package com.central.backend.service.impl;

import com.central.backend.mapper.KpnTagCategoryMapper;
import com.central.backend.service.IKpnTagCategoryService;
import com.central.common.model.KpnTagCategory;
import com.central.common.service.impl.SuperServiceImpl;
import org.springframework.stereotype.Service;
import com.central.common.model.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import lombok.extern.slf4j.Slf4j;


/**
 * 影片标签分类
 *
 * @author yixiu
 * @date 2023-02-03 16:32:41
 */
@Slf4j
@Service
public class KpnTagCategoryServiceImpl extends SuperServiceImpl<KpnTagCategoryMapper, KpnTagCategory> implements IKpnTagCategoryService {
    /**
     * 列表
     * @param params
     * @return
     */
    @Override
    public PageResult<KpnTagCategory> findList(Map<String, Object> params){
        Page<KpnTagCategory> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
        List<KpnTagCategory> list  =  baseMapper.findList(page, params);
        return PageResult.<KpnTagCategory>builder().data(list).count(page.getTotal()).build();
    }
}

package com.central.backend.service.impl;

import com.central.backend.mapper.KpnTagMapper;
import com.central.backend.service.IKpnTagService;
import com.central.common.model.KpnTag;
import com.central.common.service.impl.SuperServiceImpl;
import org.springframework.stereotype.Service;
import com.central.common.model.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

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
    /**
     * 列表
     *
     * @param params
     * @return
     */
    @Override
    public PageResult<KpnTag> findList(Map<String, Object> params){
        Page<KpnTag> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
        List<KpnTag> list  =  baseMapper.findList(page, params);
        return PageResult.<KpnTag>builder().data(list).count(page.getTotal()).build();
    }
}

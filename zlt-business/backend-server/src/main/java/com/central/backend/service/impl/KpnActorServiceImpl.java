package com.central.backend.service.impl;

import com.central.backend.mapper.KpnActorMapper;
import com.central.backend.service.IKpnActorService;
import com.central.common.model.KpnActor;
import com.central.common.service.impl.SuperServiceImpl;
import org.springframework.stereotype.Service;
import com.central.common.model.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

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
    /**
     * 列表
     * @param params
     * @return
     */
    @Override
    public PageResult<KpnActor> findList(Map<String, Object> params){
        Page<KpnActor> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
        List<KpnActor> list  =  baseMapper.findList(page, params);
        return PageResult.<KpnActor>builder().data(list).count(page.getTotal()).build();
    }
}

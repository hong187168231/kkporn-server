package com.central.backend.service.ipmanage.impl;

import com.central.backend.mapper.ipmanage.KpnBlackIpMapper;
import com.central.backend.service.ipmanage.IKpnBlackIpService;
import com.central.common.model.ipmanage.KpnBlackIp;
import com.central.common.service.impl.SuperServiceImpl;
import org.springframework.stereotype.Service;
import com.central.common.model.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import lombok.extern.slf4j.Slf4j;


/**
 * 
 *
 * @author yixiu
 * @date 2023-02-03 15:50:11
 */
@Slf4j
@Service
public class KpnBlackIpServiceImpl extends SuperServiceImpl<KpnBlackIpMapper, KpnBlackIp> implements IKpnBlackIpService {
    /**
     * 列表
     * @param params
     * @return
     */
    @Override
    public PageResult<KpnBlackIp> findList(Map<String, Object> params){
        Page<KpnBlackIp> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
        List<KpnBlackIp> list  =  baseMapper.findList(page, params);
        return PageResult.<KpnBlackIp>builder().data(list).count(page.getTotal()).build();
    }
}

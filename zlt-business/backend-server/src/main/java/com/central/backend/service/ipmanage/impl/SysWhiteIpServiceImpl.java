package com.central.backend.service.ipmanage.impl;

import com.central.backend.mapper.ipmanage.SysWhiteIpMapper;
import com.central.backend.service.ipmanage.ISysWhiteIpService;
import com.central.common.model.ipmanage.SysWhiteIp;
import org.springframework.stereotype.Service;
import com.central.common.model.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.common.service.impl.SuperServiceImpl;

import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import lombok.extern.slf4j.Slf4j;


/**
 * 
 *
 * @author yixiu
 * @date 2023-02-03 15:07:56
 */
@Slf4j
@Service
public class SysWhiteIpServiceImpl extends SuperServiceImpl<SysWhiteIpMapper, SysWhiteIp> implements ISysWhiteIpService {
    /**
     * 列表
     * @param params
     * @return
     */
    @Override
    public PageResult<SysWhiteIp> findList(Map<String, Object> params){
        Page<SysWhiteIp> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
        List<SysWhiteIp> list  =  baseMapper.findList(page, params);
        return PageResult.<SysWhiteIp>builder().data(list).count(page.getTotal()).build();
    }
}

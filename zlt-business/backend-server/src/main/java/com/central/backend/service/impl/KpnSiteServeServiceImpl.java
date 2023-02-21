package com.central.backend.service.impl;

import com.central.backend.mapper.KpnSiteServeMapper;
import com.central.backend.service.IKpnSiteServeService;
import com.central.common.model.KpnSiteServe;
import com.central.common.model.SysUser;
import org.springframework.stereotype.Service;
import com.central.common.model.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.common.service.impl.SuperServiceImpl;

import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import lombok.extern.slf4j.Slf4j;


/**
 * 站点客服配置
 *
 * @author yixiu
 * @date 2023-02-21 19:46:07
 */
@Slf4j
@Service
public class KpnSiteServeServiceImpl extends SuperServiceImpl<KpnSiteServeMapper, KpnSiteServe> implements IKpnSiteServeService {
    /**
     * 列表
     * @param params
     * @return
     */
    @Override
    public PageResult<KpnSiteServe> findList(Map<String, Object> params, SysUser user){
        if(null!=user && null!=user.getSiteId() && user.getSiteId()!=0){
            params.put("siteId",user.getSiteId());
        }
        Page<KpnSiteServe> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
        List<KpnSiteServe> list  =  baseMapper.findList(page, params);
        return PageResult.<KpnSiteServe>builder().data(list).count(page.getTotal()).build();
    }
}

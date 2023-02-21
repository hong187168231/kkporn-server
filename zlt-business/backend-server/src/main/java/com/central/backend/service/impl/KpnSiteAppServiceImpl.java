package com.central.backend.service.impl;

import com.central.backend.mapper.KpnSiteAppMapper;
import com.central.backend.service.IKpnSiteAppService;
import com.central.common.model.KpnSiteApp;
import org.springframework.stereotype.Service;
import com.central.common.model.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.common.service.impl.SuperServiceImpl;

import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import lombok.extern.slf4j.Slf4j;


/**
 * 站点app更新配置
 *
 * @author yixiu
 * @date 2023-02-21 19:46:07
 */
@Slf4j
@Service
public class KpnSiteAppServiceImpl extends SuperServiceImpl<KpnSiteAppMapper, KpnSiteApp> implements IKpnSiteAppService {
    /**
     * 列表
     * @param params
     * @return
     */
    @Override
    public PageResult<KpnSiteApp> findList(Map<String, Object> params){
        Page<KpnSiteApp> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
        List<KpnSiteApp> list  =  baseMapper.findList(page, params);
        return PageResult.<KpnSiteApp>builder().data(list).count(page.getTotal()).build();
    }
}

package com.central.backend.service;

import com.central.common.model.KpnSiteServe;
import com.central.common.model.PageResult;
import com.central.common.model.SysUser;
import com.central.common.service.ISuperService;

import java.util.Map;

/**
 * 站点客服配置
 *
 * @author yixiu
 * @date 2023-02-21 19:46:07
 */
public interface IKpnSiteServeService extends ISuperService<KpnSiteServe> {
    /**
     * 列表
     * @param params
     * @return
     */
    PageResult<KpnSiteServe> findList(Map<String, Object> params, SysUser user);
}


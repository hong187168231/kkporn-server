package com.central.porn.service;

import com.central.common.model.KpnSite;
import com.central.common.service.ISuperService;


public interface IKpnSiteService extends ISuperService<KpnSite> {

    /**
     * 根据referer查询
     * @param referer
     * @return
     */
    KpnSite getInfoByReferer(String referer);
}

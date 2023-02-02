package com.central.porn.service;

import com.central.common.model.KpnSiteChannel;
import com.central.common.service.ISuperService;

import java.util.List;


public interface IKpnSiteChannelService extends ISuperService<KpnSiteChannel> {

    /**
     * 获取站点频道
     * @param id
     */
    List<KpnSiteChannel> getBySiteId(Long id);
}

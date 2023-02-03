package com.central.porn.service;

import com.central.common.model.KpnSiteChannel;
import com.central.common.service.ISuperService;

import java.util.List;


public interface IKpnSiteChannelService extends ISuperService<KpnSiteChannel> {

    /**
     * 获取站点频道
     *
     * @param id 站点id
     */
    List<KpnSiteChannel> getBySiteId(Long id);

    /**
     * 获取会员频道
     * @param uid 会员id
     * @return
     */
    List<KpnSiteChannel> getMemberChannels(Long uid);
}

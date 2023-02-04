package com.central.porn.service;

import com.central.common.model.KpnSiteTopic;
import com.central.common.service.ISuperService;
import com.central.porn.entity.vo.KpnSiteTopicVo;

import java.util.List;


public interface IKpnSiteTopicService extends ISuperService<KpnSiteTopic> {

    List<KpnSiteTopicVo> getBySiteId(Long id);
}

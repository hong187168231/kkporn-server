package com.central.porn.service;

import com.central.common.model.KpnSiteTopic;
import com.central.common.service.ISuperService;

import java.util.List;


public interface IKpnSiteTopicService extends ISuperService<KpnSiteTopic> {

    List<KpnSiteTopic> getBySiteId(Long id);
}

package com.central.porn.service.impl;

import com.central.common.model.KpnSiteAdvertise;
import com.central.common.model.enums.SiteAdDeviceEnum;
import com.central.common.model.enums.SiteAdPositionEnum;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.mapper.KpnSiteAdvertiseMapper;
import com.central.porn.service.IKpnSiteAdvertiseService;
import com.central.porn.service.IKpnSiteTopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class KpnSiteAdvertiseServiceImpl extends SuperServiceImpl<KpnSiteAdvertiseMapper, KpnSiteAdvertise> implements IKpnSiteAdvertiseService {

    @Autowired
    private IKpnSiteTopicService siteTopicService;

    @Override
    public List<KpnSiteAdvertise> getSiteAdvertise(Long sid) {
        List<KpnSiteAdvertise> kpnSiteAdvertises = this.lambdaQuery()
                .eq(KpnSiteAdvertise::getSiteId, sid)
                .eq(KpnSiteAdvertise::getStatus, true)
                .ne(KpnSiteAdvertise::getPosition, SiteAdPositionEnum.TOPIC.getCode())
                .le(KpnSiteAdvertise::getStartTime, new Date())
                .ge(KpnSiteAdvertise::getEndTime, new Date())
                .orderByDesc(KpnSiteAdvertise::getSort)
                .orderByDesc(KpnSiteAdvertise::getCreateTime)
                .list();

        //主题广告
        int topicSize = siteTopicService.getBySiteId(sid).size();
        if (topicSize == 0) {
            return kpnSiteAdvertises;
        }

        for (SiteAdDeviceEnum adPlatformEnum : SiteAdDeviceEnum.values()) {
            List<KpnSiteAdvertise> deviceTopicAdvertises = this.lambdaQuery()
                    .eq(KpnSiteAdvertise::getSiteId, sid)
                    .eq(KpnSiteAdvertise::getStatus, true)
                    .eq(KpnSiteAdvertise::getDevice, adPlatformEnum.getDevice())
                    .eq(KpnSiteAdvertise::getPosition, SiteAdPositionEnum.TOPIC.getCode())
                    .le(KpnSiteAdvertise::getStartTime, new Date())
                    .ge(KpnSiteAdvertise::getEndTime, new Date())
                    .orderByDesc(KpnSiteAdvertise::getSort)
                    .orderByDesc(KpnSiteAdvertise::getCreateTime)
                    .last(" limit " + topicSize)
                    .list();
            if (deviceTopicAdvertises.size() == 0) {
                continue;
            }

            //跟专题数量一致
            if (deviceTopicAdvertises.size() < topicSize) {
                for (int i = 0; i < deviceTopicAdvertises.size(); i++) {
                    if (deviceTopicAdvertises.size() >= topicSize) {
                        break;
                    }
                    deviceTopicAdvertises.add(deviceTopicAdvertises.get(i));
                }
            }
            kpnSiteAdvertises.addAll(deviceTopicAdvertises);
        }

        return kpnSiteAdvertises;
    }
}

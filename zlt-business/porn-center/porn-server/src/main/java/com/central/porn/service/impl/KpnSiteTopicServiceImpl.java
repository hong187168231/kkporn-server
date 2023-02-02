package com.central.porn.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.central.common.constant.PornConstants;
import com.central.common.model.KpnSiteTopic;
import com.central.common.model.enums.SiteTopicEnum;
import com.central.common.redis.template.RedisRepository;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.mapper.KpnSiteTopicMapper;
import com.central.porn.service.IKpnSiteTopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class KpnSiteTopicServiceImpl extends SuperServiceImpl<KpnSiteTopicMapper, KpnSiteTopic> implements IKpnSiteTopicService {

    @Override
    public List<KpnSiteTopic> getBySiteId(Long sid) {

        String topicKey = StrUtil.format(PornConstants.RedisKey.SITE_TOPIC_KEY, sid);
        List<KpnSiteTopic> siteTopicList = (List<KpnSiteTopic>) RedisRepository.get(topicKey);
        if (CollectionUtil.isEmpty(siteTopicList)) {
            siteTopicList = this.lambdaQuery()
                    .eq(KpnSiteTopic::getSiteId, sid)
                    .eq(KpnSiteTopic::getStatus, SiteTopicEnum.ON_SHELF.getStatus())
                    .list();

            RedisRepository.set(topicKey, siteTopicList);
        }

        return siteTopicList;
    }
}

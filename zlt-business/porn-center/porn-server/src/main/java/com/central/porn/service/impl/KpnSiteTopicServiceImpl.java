package com.central.porn.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.central.common.constant.PornConstants;
import com.central.common.model.KpnSiteTopic;
import com.central.common.model.enums.SiteTopicEnum;
import com.central.common.redis.template.RedisRepository;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.entity.vo.KpnSiteTopicVo;
import com.central.porn.mapper.KpnSiteTopicMapper;
import com.central.porn.service.IKpnSiteTopicMovieService;
import com.central.porn.service.IKpnSiteTopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class KpnSiteTopicServiceImpl extends SuperServiceImpl<KpnSiteTopicMapper, KpnSiteTopic> implements IKpnSiteTopicService {

    @Autowired
    private IKpnSiteTopicMovieService topicMovieService;

    @Override
    public List<KpnSiteTopicVo> getBySiteId(Long sid) {
        //查询并缓存专题
        String topicKey = StrUtil.format(PornConstants.RedisKey.SITE_TOPIC_KEY, sid);
        List<KpnSiteTopicVo> siteTopicVos = (List<KpnSiteTopicVo>) RedisRepository.get(topicKey);
        if (CollectionUtil.isEmpty(siteTopicVos)) {
            List<KpnSiteTopic> siteTopicList = this.lambdaQuery()
                    .eq(KpnSiteTopic::getSiteId, sid)
                    .eq(KpnSiteTopic::getStatus, SiteTopicEnum.ON_SHELF.getStatus())
                    .list();
            if (CollectionUtil.isNotEmpty(siteTopicList)) {
                siteTopicVos = siteTopicList.stream().map(kpnSiteTopic -> {
                    KpnSiteTopicVo topicVo = new KpnSiteTopicVo();
                    BeanUtil.copyProperties(kpnSiteTopic, topicVo);

                    //查询关联最新的5部影片
                    List<Long> movieIds = topicMovieService.getMovieIds(sid, topicVo.getId(), 1, 5);
                    topicVo.setMovieIds(movieIds);
                    return topicVo;
                }).collect(Collectors.toList());
            }
        }

        return siteTopicVos;
    }
}




















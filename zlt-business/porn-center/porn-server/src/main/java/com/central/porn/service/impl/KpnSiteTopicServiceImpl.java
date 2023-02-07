package com.central.porn.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.central.common.constant.PornConstants;
import com.central.common.model.KpnSiteTopic;
import com.central.common.model.enums.SiteTopicEnum;
import com.central.common.redis.template.RedisRepository;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.core.language.LanguageUtil;
import com.central.porn.entity.vo.KpnSiteMovieBaseVo;
import com.central.porn.entity.vo.KpnSiteTopicVo;
import com.central.porn.mapper.KpnSiteTopicMapper;
import com.central.porn.service.IKpnMovieService;
import com.central.porn.service.IKpnSiteMovieService;
import com.central.porn.service.IKpnSiteTopicMovieService;
import com.central.porn.service.IKpnSiteTopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class KpnSiteTopicServiceImpl extends SuperServiceImpl<KpnSiteTopicMapper, KpnSiteTopic> implements IKpnSiteTopicService {

    @Autowired
    private IKpnSiteTopicMovieService topicMovieService;

    @Autowired
    private IKpnSiteMovieService siteMovieService;

    @Autowired
    private IKpnMovieService movieService;

    @Override
    public List<KpnSiteTopicVo> getBySiteId(Long sid) {
        //todo 如何对vo缓存 vv,favorites实时更新
//        List<KpnSiteTopicVo> kpnSiteTopicVos = (List<KpnSiteTopicVo>) RedisRepository.get("CACHE:VO:SITE:TOPICS:1");
//        if (CollectionUtil.isNotEmpty(kpnSiteTopicVos)) {
//            return kpnSiteTopicVos;
//        }
        //查询并缓存专题
        String topicKey = StrUtil.format(PornConstants.RedisKey.SITE_TOPIC_KEY, sid);
        List<KpnSiteTopic> siteTopics = (List<KpnSiteTopic>) RedisRepository.get(topicKey);
        if (CollectionUtil.isEmpty(siteTopics)) {
            siteTopics = this.lambdaQuery()
                    .eq(KpnSiteTopic::getSiteId, sid)
                    .eq(KpnSiteTopic::getStatus, SiteTopicEnum.ON_SHELF.getStatus())
                    .orderByDesc(KpnSiteTopic::getSort)
                    .list();
            if (CollectionUtil.isNotEmpty(siteTopics)) {
                RedisRepository.setExpire(topicKey, siteTopics, PornConstants.RedisKey.EXPIRE_TIME_30_DAYS, TimeUnit.SECONDS);
            }
        }

        return siteTopics.stream().map(kpnSiteTopic -> {
            KpnSiteTopicVo topicVo = new KpnSiteTopicVo();
            BeanUtil.copyProperties(kpnSiteTopic, topicVo);
            topicVo.setName(LanguageUtil.getLanguageName(topicVo));
            //查询关联最新的5部影片
            List<Long> movieIds = topicMovieService.getMovieIds(sid, topicVo.getId(), 1, 5);
            List<KpnSiteMovieBaseVo> kpnSiteMovieBaseVos = siteMovieService.getSiteMovieByIds(sid, movieIds);
            topicVo.setMovieBaseVos(kpnSiteMovieBaseVos);
            return topicVo;
        }).collect(Collectors.toList());
//        if (CollectionUtil.isNotEmpty(kpnSiteTopicVos)) {
//            RedisRepository.setExpire("CACHE:VO:SITE:TOPICS:1", kpnSiteTopicVos, PornConstants.RedisKey.EXPIRE_TIME_30_DAYS, TimeUnit.SECONDS);
//        }
//        return kpnSiteTopicVos;
    }
}




















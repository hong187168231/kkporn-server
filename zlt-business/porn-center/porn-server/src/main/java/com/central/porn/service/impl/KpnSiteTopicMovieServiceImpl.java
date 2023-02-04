package com.central.porn.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.central.common.constant.PornConstants;
import com.central.common.model.KpnSiteTopicMovie;
import com.central.common.redis.template.RedisRepository;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.mapper.KpnSiteTopicMovieMapper;
import com.central.porn.service.IKpnSiteTopicMovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class KpnSiteTopicMovieServiceImpl extends SuperServiceImpl<KpnSiteTopicMovieMapper, KpnSiteTopicMovie> implements IKpnSiteTopicMovieService {

    @Override
    public List<Long> getMovieIds(Long sid, Long topicId, Integer currPage, Integer size) {
        String topicMovieIdRedisKey = StrUtil.format(PornConstants.RedisKey.SITE_TOPIC_MOVIEID_KEY, sid, topicId);

        int start = (currPage - 1) * size;
        int end = start + size - 1;
        List<String> topicMovieIdList = (ArrayList) RedisRepository.getList(topicMovieIdRedisKey, start, end);
        if (CollectionUtil.isEmpty(topicMovieIdList)) {
            List<KpnSiteTopicMovie> siteTopicMovies = this.lambdaQuery()
                    .eq(KpnSiteTopicMovie::getTopicId, topicId)
                    .orderByAsc(KpnSiteTopicMovie::getId)
                    .list();

            if (CollectionUtil.isNotEmpty(siteTopicMovies)) {
                List<String> topicMovieIds = siteTopicMovies.stream()
                        .map(kpnSiteTopicMovie -> String.valueOf(kpnSiteTopicMovie.getMovieId()))
                        .collect(Collectors.toList());
                RedisRepository.leftPushAll(topicMovieIdRedisKey, topicMovieIds);
            }

            topicMovieIdList = (ArrayList) RedisRepository.getList(topicMovieIdRedisKey, start, end);
        }
        return topicMovieIdList.stream().map(Long::parseLong).collect(Collectors.toList());
    }
}




















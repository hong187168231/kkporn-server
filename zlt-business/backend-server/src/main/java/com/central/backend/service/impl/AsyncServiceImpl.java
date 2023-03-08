package com.central.backend.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.central.backend.service.IAsyncService;
import com.central.common.constant.PornConstants;
import com.central.common.redis.template.RedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AsyncServiceImpl implements IAsyncService {

    @Async
    @Override
    public void setVipExpire(Date newVipExpire, Long userId) {
        try {
            long expireInSeconds = (newVipExpire.getTime() - new Date().getTime()) / 1000;
            String vipExpireKey = StrUtil.format(PornConstants.RedisKey.KPN_SITE_VIP_EXPIRE, userId);
            RedisRepository.setExpire(vipExpireKey, DateUtil.formatDateTime(newVipExpire), expireInSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Async
    @Override
    public void delActorCache(Long actorId) {
        try {
            RedisRepository.delete(StrUtil.format(PornConstants.RedisKey.KPN_ACTOR_KEY, actorId));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Async
    @Override
    public void deleteSiteTopicCache(Long sid) {
        try {
            RedisRepository.delete(StrUtil.format(PornConstants.RedisKey.KPN_SITE_TOPIC_KEY, sid));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Async
    @Override
    public void deleteSiteTopicMovieCache(Long sid, Long topicId) {
        try {
            RedisRepository.delete(StrUtil.format(PornConstants.RedisKey.KPN_SITE_TOPIC_MOVIEID_SORT_KEY, sid, topicId));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Async
    @Override
    public void deleteSiteInfoCache(Long sid) {
        try {
            RedisRepository.delete(PornConstants.RedisKey.KPN_SITE_LIST_KEY);
            if (ObjectUtil.isNotEmpty(sid)) {
                RedisRepository.delete(StrUtil.format(PornConstants.RedisKey.KPN_SITE_INFO_KEY, sid));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Async
    @Override
    public void deleteLinesCache() {
        try {
            RedisRepository.delete(PornConstants.RedisKey.KPN_LINE);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}

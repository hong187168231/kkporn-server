package com.central.backend.service.impl;

import cn.hutool.core.date.DateUtil;
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
        long expireInSeconds = (newVipExpire.getTime() - new Date().getTime()) / 1000;
        String vipExpireKey = StrUtil.format(PornConstants.RedisKey.KPN_SITE_VIP_EXPIRE, userId);
        RedisRepository.setExpire(vipExpireKey, DateUtil.formatDateTime(newVipExpire), expireInSeconds, TimeUnit.SECONDS);
    }
}

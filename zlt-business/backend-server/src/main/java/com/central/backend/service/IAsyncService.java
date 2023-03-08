package com.central.backend.service;

import java.util.Date;

public interface IAsyncService {

    /**
     * 设置最新过期时间
     *
     * @param newVipExpire 最新过期时间
     * @param userId       会员id
     */
    void setVipExpire(Date newVipExpire, Long userId);

    /**
     * 删除演员cache
     *
     * @param actorId 演员id
     */
    void delActorCache(Long actorId);
}

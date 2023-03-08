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

    /**
     * 删除站点专题缓存
     *
     * @param sid
     */
    void deleteSiteTopicCache(Long sid);

    /**
     * 删除站点专题影片
     *
     * @param sid     站点id
     * @param topicId 站提id
     */
    void deleteSiteTopicMovieCache(Long sid, Long topicId);
}

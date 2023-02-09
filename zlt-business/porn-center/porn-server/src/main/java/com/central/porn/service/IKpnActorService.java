package com.central.porn.service;

import com.central.common.model.KpnActor;
import com.central.common.service.ISuperService;


public interface IKpnActorService extends ISuperService<KpnActor> {

    /**
     * 获取演员信息
     *
     * @param actorId 演员id
     * @return
     */
    KpnActor getActorById(Long actorId);

    /**
     * 增加总关注量
     *
     * @param actorId 演员id
     */
    void addActorFavorites(Long actorId);

    /**
     * 减去总关注量
     *
     * @param actorId 演员id
     */
    void removeActorFavorites(Long actorId);
}

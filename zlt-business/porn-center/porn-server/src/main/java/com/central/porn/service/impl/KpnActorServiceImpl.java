package com.central.porn.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.central.common.constant.PornConstants;
import com.central.common.model.KpnActor;
import com.central.common.model.KpnMovie;
import com.central.common.redis.template.RedisRepository;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.core.language.LanguageUtil;
import com.central.porn.entity.vo.KpnActorVo;
import com.central.porn.mapper.KpnActorMapper;
import com.central.porn.service.IKpnActorService;
import com.central.porn.service.IKpnSiteActorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Slf4j
@Service
public class KpnActorServiceImpl extends SuperServiceImpl<KpnActorMapper, KpnActor> implements IKpnActorService {

    @Override
    public KpnActor getActorById(Long actorId) {
        String redisActorKey = StrUtil.format(PornConstants.RedisKey.KPN_ACTOR_KEY, actorId);
        KpnActor actor = (KpnActor) RedisRepository.get(redisActorKey);
        if (ObjectUtil.isEmpty(actor)) {
            actor = getById(actorId);
            if (ObjectUtil.isNotEmpty(actor)) {
                RedisRepository.setExpire(redisActorKey, actor, PornConstants.RedisKey.EXPIRE_TIME_30_DAYS);
            }
        }
        return actor;
    }

    @Async
    @Override
    public void addActorFavorites(Long actorId) {
        this.lambdaUpdate()
                .eq(KpnActor::getId, actorId)
                .setSql(" `favorites` = `favorites` + 1")
                .update();
    }

    @Async
    @Override
    public void removeActorFavorites(Long actorId) {
        this.lambdaUpdate()
                .eq(KpnActor::getId, actorId)
                .setSql(" `favorites` = `favorites` - 1")
                .update();
    }
}

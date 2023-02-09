package com.central.porn.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.central.common.constant.PornConstants;
import com.central.common.model.KpnSiteActor;
import com.central.common.model.KpnSiteUserActorFavorites;
import com.central.common.redis.lock.RedissLockUtil;
import com.central.common.redis.template.RedisRepository;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.mapper.KpnSiteActorMapper;
import com.central.porn.service.IAsyncService;
import com.central.porn.service.IKpnActorService;
import com.central.porn.service.IKpnSiteActorService;
import com.central.porn.service.IKpnSiteUserActorFavoritesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KpnSiteActorServiceImpl extends SuperServiceImpl<KpnSiteActorMapper, KpnSiteActor> implements IKpnSiteActorService {

    @Autowired
    private IAsyncService asyncService;

    @Autowired
    private IKpnSiteUserActorFavoritesService userActorFavoritesService;

    @Autowired
    private IKpnActorService actorService;

    @Override
    public Long getSiteActorFavorites(Long sid, Long actorId) {
        String siteActorFavoritesKey = StrUtil.format(PornConstants.RedisKey.KPN_SITE_ACTOR_FAVORITES_KEY, sid, actorId);
        cacheSiteActorFavorites(siteActorFavoritesKey, sid, actorId);

        long favorites = RedisRepository.incr(siteActorFavoritesKey);
        RedisRepository.decr(siteActorFavoritesKey);

        return favorites - 1;
    }

    @Override
    public Long addSiteActorFavorites(Long sid, Long userId, Long actorId) {
        KpnSiteUserActorFavorites userActorFavorites = userActorFavoritesService.getUserActorFavorites(userId, actorId);
        if (ObjectUtil.isNotEmpty(userActorFavorites)) {
            throw new RuntimeException("已经收藏该演员,不可重复操作");
        }

        //获取站点收藏量
        String siteActorFavoritesKey = StrUtil.format(PornConstants.RedisKey.KPN_SITE_ACTOR_FAVORITES_KEY, sid, actorId);
        cacheSiteActorFavorites(siteActorFavoritesKey, sid, actorId);

        //增加站点演员收藏量
        asyncService.addSiteActorFavorites(sid, actorId);

        //增加演员总收藏量
        actorService.addActorFavorites(actorId);

        //增加会员收藏演员
        userActorFavoritesService.add(userId, actorId);

        return RedisRepository.incr(siteActorFavoritesKey);
    }

    @Override
    public Long removeSiteActorFavorites(Long sid, Long userId, Long actorId) {
        KpnSiteUserActorFavorites userActorFavorites = userActorFavoritesService.getUserActorFavorites(userId, actorId);
        if (ObjectUtil.isEmpty(userActorFavorites)) {
            throw new RuntimeException("未收藏该演员,不可操作");
        }

        //获取站点收藏量
        String siteActorFavoritesKey = StrUtil.format(PornConstants.RedisKey.KPN_SITE_ACTOR_FAVORITES_KEY, sid, actorId);
        cacheSiteActorFavorites(siteActorFavoritesKey, sid, actorId);

        //减去站点演员收藏量
        asyncService.removeSiteActorFavorites(sid, actorId);

        //减去演员总收藏量
        actorService.removeActorFavorites(actorId);

        //减去会员收藏演员
        userActorFavoritesService.remove(userId, actorId);

        return RedisRepository.decr(siteActorFavoritesKey);
    }

    private void cacheSiteActorFavorites(String siteActorFavoritesKey, Long sid, Long actorId) {
        Object siteActorFavoritesObj = RedisRepository.get(siteActorFavoritesKey);

        if (ObjectUtil.isEmpty(siteActorFavoritesObj)) {
            String lockKey = StrUtil.format("Lock:" + siteActorFavoritesKey, sid, actorId);
            boolean lockedSuccess = RedissLockUtil.tryLock(lockKey, PornConstants.Lock.WAIT_TIME, PornConstants.Lock.LEASE_TIME);
            if (!lockedSuccess) {
                throw new RuntimeException("加锁失败");
            }
            try {
                siteActorFavoritesObj = RedisRepository.get(siteActorFavoritesKey);
                if (ObjectUtil.isEmpty(siteActorFavoritesObj)) {
                    KpnSiteActor siteActor = this.lambdaQuery()
                            .eq(KpnSiteActor::getSiteId, sid)
                            .eq(KpnSiteActor::getActorId, actorId)
                            .one();
                    Long favorites = 0L;
                    if (ObjectUtil.isEmpty(siteActor)) {
                        KpnSiteActor newKpnSiteActor = KpnSiteActor.builder().siteId(sid).actorId(actorId).favorites(favorites).build();
                        save(newKpnSiteActor);
                    } else {
                        favorites = siteActor.getFavorites();
                    }
                    RedisRepository.setExpire(siteActorFavoritesKey, favorites, PornConstants.RedisKey.EXPIRE_TIME_30_DAYS);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                RedissLockUtil.unlock(lockKey);
            }
        }
    }
}

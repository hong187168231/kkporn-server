package com.central.porn.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.central.common.constant.PornConstants;
import com.central.common.model.KpnActor;
import com.central.common.model.KpnSiteActor;
import com.central.common.model.KpnSiteUserActorFavorites;
import com.central.common.redis.lock.RedissLockUtil;
import com.central.common.redis.template.RedisRepository;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.core.language.LanguageUtil;
import com.central.porn.entity.vo.KpnActorVo;
import com.central.porn.enums.KpnSortOrderEnum;
import com.central.porn.mapper.KpnSiteActorMapper;
import com.central.porn.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class KpnSiteActorServiceImpl extends SuperServiceImpl<KpnSiteActorMapper, KpnSiteActor> implements IKpnSiteActorService {

    @Autowired
    private IAsyncService asyncService;

    @Autowired
    private IKpnSiteUserActorFavoritesService userActorFavoritesService;

    @Autowired
    private IKpnActorService actorService;

//    @Autowired
//    private IKpnSiteActorService siteActorService;

    @Autowired
    private IKpnSiteMovieService siteMovieService;

    @Override
    public KpnActorVo getKpnActorVo(Long sid, Long actorId) {
        //获取影片演员信息
        KpnActor kpnActor = actorService.getActorByIds(Collections.singletonList(actorId)).get(0);
        KpnActorVo kpnActorVo = new KpnActorVo();
        BeanUtil.copyProperties(kpnActor, kpnActorVo);
        kpnActorVo.setName(LanguageUtil.getLanguageName(kpnActorVo));
        //站点演员收藏量
        Long siteActorFavorites = getSiteActorFavorites(sid, kpnActor.getId());
        kpnActorVo.setFavorites(siteActorFavorites);
        //站点演员影片数量
        Long siteActorMovieNum = siteMovieService.getSiteActorMovieNum(sid, actorId);
        kpnActorVo.setMovieNum(siteActorMovieNum);

        return kpnActorVo;
    }

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

    @Override
    public List<KpnActorVo> getActorListByFavorites(Long sid, String sortOrder, Integer currPage, Integer pageSize) {
        Integer startIndex = (currPage - 1) * pageSize;
        List<Long> actorIds = baseMapper.getActorListByFavorites(sid, sortOrder, startIndex, pageSize);
        if (CollectionUtil.isEmpty(actorIds)) {
            return new ArrayList<>();
        }
        List<KpnActor> kpnActors = actorService.getActorByIds(actorIds);

        return kpnActors.stream().map(kpnActor -> {
            KpnActorVo kpnActorVo = new KpnActorVo();
            BeanUtil.copyProperties(kpnActor, kpnActorVo);
            kpnActorVo.setName(LanguageUtil.getLanguageName(kpnActorVo));
            return kpnActorVo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<KpnActorVo> getActorListByCreateTime(Long sid, String sortOrder, Integer currPage, Integer pageSize) {
        Integer startIndex = (currPage - 1) * pageSize;
        List<Long> actorIds = baseMapper.getActorListByCreateTime(sid, sortOrder, startIndex, pageSize);
        if (CollectionUtil.isEmpty(actorIds)) {
            return new ArrayList<>();
        }

        List<KpnActor> kpnActors = actorService.getActorByIds(actorIds);

        return kpnActors.stream().map(kpnActor -> {
            KpnActorVo kpnActorVo = new KpnActorVo();
            BeanUtil.copyProperties(kpnActor, kpnActorVo);
            kpnActorVo.setName(LanguageUtil.getLanguageName(kpnActorVo));
            return kpnActorVo;
        }).collect(Collectors.toList());
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

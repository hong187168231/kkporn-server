package com.central.porn.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.common.constant.PornConstants;
import com.central.common.model.KpnActor;
import com.central.common.model.KpnMovie;
import com.central.common.model.KpnSiteMovie;
import com.central.common.model.KpnSiteUserMovieFavorites;
import com.central.common.model.enums.SiteMovieStatusEnum;
import com.central.common.redis.lock.RedissLockUtil;
import com.central.common.redis.template.RedisRepository;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.core.language.LanguageUtil;
import com.central.porn.entity.vo.KpnMovieVo;
import com.central.porn.entity.vo.KpnSiteMovieBaseVo;
import com.central.porn.entity.vo.KpnTagVo;
import com.central.porn.enums.KpnSortOrderEnum;
import com.central.porn.enums.KpnMovieSortTypeEnum;
import com.central.porn.mapper.KpnSiteMovieMapper;
import com.central.porn.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class KpnSiteMovieServiceImpl extends SuperServiceImpl<KpnSiteMovieMapper, KpnSiteMovie> implements IKpnSiteMovieService {

    @Autowired
    private IKpnMovieService movieService;

    @Autowired
    private IAsyncService asyncService;

    @Autowired
    private IKpnMovieTagService movieTagService;

    @Autowired
    private IKpnSiteMovieService siteMovieService;

    @Autowired
    private IKpnSiteUserMovieFavoritesService siteUserMovieFavoritesService;

    @Autowired
    private IKpnActorService actorService;

    @Autowired
    private IKpnSiteActorService siteActorService;

    @Override
    public List<KpnSiteMovieBaseVo> getSiteMovieByIds(Long sid, List<Long> movieIds) {
        List<KpnSiteMovieBaseVo> siteMovieVos = new ArrayList<>();
        //从缓存中查询影片信息
        for (Long movieId : movieIds) {
            String movieRedisKey = StrUtil.format(PornConstants.RedisKey.KPN_MOVIEID_KEY, movieId);
            KpnMovie kpnMovie = (KpnMovie) RedisRepository.get(movieRedisKey);

            //缓存影片信息
            if (ObjectUtil.isEmpty(kpnMovie)) {
                kpnMovie = movieService.lambdaQuery()
                        .eq(KpnMovie::getHandleStatus, true)
                        .eq(KpnMovie::getStatus, true)
                        .in(KpnMovie::getId, movieId)
                        .one();
                if (ObjectUtil.isNotEmpty(kpnMovie)) {
                    RedisRepository.setExpire(movieRedisKey, kpnMovie, PornConstants.RedisKey.EXPIRE_TIME_30_DAYS);
                }
            }

            if (ObjectUtil.isNotEmpty(kpnMovie)) {
                KpnSiteMovieBaseVo kpnSiteMovieBaseVo = new KpnSiteMovieBaseVo();
                BeanUtil.copyProperties(kpnMovie, kpnSiteMovieBaseVo);
                kpnSiteMovieBaseVo.setName(LanguageUtil.getLanguageName(kpnSiteMovieBaseVo));

                //获取标签信息
                List<KpnTagVo> kpnTagVos = movieTagService.getTagByMovieId(kpnMovie.getId());
                kpnSiteMovieBaseVo.setTagVos(kpnTagVos);
                siteMovieVos.add(kpnSiteMovieBaseVo);

                //获取播放量
                KpnSiteMovie siteMovie = siteMovieService.getSiteMovie(sid, kpnMovie.getId());
                kpnSiteMovieBaseVo.setVv(siteMovie.getVv());
                kpnSiteMovieBaseVo.setFavorites(siteMovie.getFavorites());
            }
        }

        return siteMovieVos;
    }

    @Override
    public KpnMovieVo getSiteMovieDetail(Long sid, Long movieId) {
        List<KpnSiteMovieBaseVo> siteMovieVos = new ArrayList<>();

        //从缓存中查询影片信息
        String movieRedisKey = StrUtil.format(PornConstants.RedisKey.KPN_MOVIEID_KEY, movieId);
        KpnMovie kpnMovie = (KpnMovie) RedisRepository.get(movieRedisKey);

        //缓存影片信息
        if (ObjectUtil.isEmpty(kpnMovie)) {
            kpnMovie = movieService.lambdaQuery()
                    .eq(KpnMovie::getHandleStatus, true)
                    .eq(KpnMovie::getStatus, true)
                    .in(KpnMovie::getId, movieId)
                    .one();
            if (ObjectUtil.isNotEmpty(kpnMovie)) {
                RedisRepository.setExpire(movieRedisKey, kpnMovie, PornConstants.RedisKey.EXPIRE_TIME_30_DAYS);
            }
        }

        KpnMovieVo kpnMovieVo = new KpnMovieVo();
        if (ObjectUtil.isNotEmpty(kpnMovie)) {
            BeanUtil.copyProperties(kpnMovie, kpnMovieVo);
            kpnMovieVo.setName(LanguageUtil.getLanguageName(kpnMovieVo));

            //获取影片标签信息
            List<KpnTagVo> kpnTagVos = movieTagService.getTagByMovieId(kpnMovie.getId());
            kpnMovieVo.setTagVos(kpnTagVos);
            siteMovieVos.add(kpnMovieVo);

            //获取播放量
            KpnSiteMovie siteMovie = siteMovieService.getSiteMovie(sid, kpnMovie.getId());
            kpnMovieVo.setVv(siteMovie.getVv());
            kpnMovieVo.setFavorites(siteMovie.getFavorites());
        }
        return kpnMovieVo;
    }

    @Override
    public KpnSiteMovie getSiteMovie(Long sid, Long movieId) {
        //先缓存
        String siteMovieVvKey = StrUtil.format(PornConstants.RedisKey.KPN_SITE_MOVIE_VV_KEY, sid, movieId);
        cacheSiteMovieVv(siteMovieVvKey, sid, movieId);
        long vv = RedisRepository.incr(siteMovieVvKey);
        RedisRepository.decr(siteMovieVvKey);

        String siteMovieFavoritesKey = StrUtil.format(PornConstants.RedisKey.KPN_SITE_MOVIE_FAVORITES_KEY, sid, movieId);
        cacheSiteMovieFavorites(siteMovieFavoritesKey, sid, movieId);
        long favorites = RedisRepository.incr(siteMovieFavoritesKey);
        RedisRepository.decr(siteMovieFavoritesKey);

        return KpnSiteMovie.builder().siteId(sid).movieId(movieId).vv(vv - 1).favorites(favorites - 1).build();
    }

    @Override
    public Long addSiteMovieVv(Long sid, Long movieId) {
        //先缓存
        String siteMovieVvKey = StrUtil.format(PornConstants.RedisKey.KPN_SITE_MOVIE_VV_KEY, sid, movieId);
        cacheSiteMovieVv(siteMovieVvKey, sid, movieId);

        //异步增加站点影片播放量
        asyncService.addSiteMovieVv(sid, movieId);

        // 异步增加影片总播放量
        movieService.addMovieVv(movieId);

        return RedisRepository.incr(siteMovieVvKey);
    }

    @Override
    public Long addSiteMovieFavorites(Long sid, Long userId, Long movieId) {
        KpnSiteUserMovieFavorites movie = siteUserMovieFavoritesService.getUserMovieFavorites(userId, movieId);
        if (ObjectUtil.isNotEmpty(movie)) {
            throw new RuntimeException("已经收藏该影片,不可重复操作");
        }

        String siteMovieFavoritesKey = StrUtil.format(PornConstants.RedisKey.KPN_SITE_MOVIE_FAVORITES_KEY, sid, movieId);
        cacheSiteMovieFavorites(siteMovieFavoritesKey, sid, movieId);

        //增加站点影片收藏量
        asyncService.addSiteMovieFavorites(sid, movieId);

        //增加影片总收藏量
        movieService.addMovieFavorites(movieId);

        //增加会员收藏影片
        siteUserMovieFavoritesService.add(userId, movieId);

        return RedisRepository.incr(siteMovieFavoritesKey);
    }

    @Override
    public Long removeSiteMovieFavorites(Long sid, Long userId, Long movieId) {
        KpnSiteUserMovieFavorites movie = siteUserMovieFavoritesService.getUserMovieFavorites(userId, movieId);
        if (ObjectUtil.isEmpty(movie)) {
            throw new RuntimeException("会员未收藏,不可取消");
        }

        String siteMovieFavoritesKey = StrUtil.format(PornConstants.RedisKey.KPN_SITE_MOVIE_FAVORITES_KEY, sid, movieId);
        cacheSiteMovieFavorites(siteMovieFavoritesKey, sid, movieId);

        //删除站点影片收藏量
        asyncService.removeSiteMovieFavorites(sid, movieId);

        //删除影片总收藏量
        movieService.removeMovieFavorites(movieId);

        //移除会员收藏影片
        siteUserMovieFavoritesService.remove(userId, movieId);

        return RedisRepository.decr(siteMovieFavoritesKey);
    }

    @Override
    public Long getSiteActorMovieNum(Long sid, Long actorId) {
        String redisKey = StrUtil.format(PornConstants.RedisKey.KPN_SITE_ACTOR_MOVIENUM_KEY, sid, actorId);
        Integer siteActorMovieNum = (Integer) RedisRepository.get(redisKey);
        if (ObjectUtil.isEmpty(siteActorMovieNum)) {
            siteActorMovieNum = this.lambdaQuery().eq(KpnSiteMovie::getSiteId, sid).eq(KpnSiteMovie::getActorId, actorId).count();

            RedisRepository.setExpire(redisKey, siteActorMovieNum, PornConstants.RedisKey.EXPIRE_TIME_30_DAYS);
        }

        return siteActorMovieNum.longValue();
    }

    @Override
    public List<KpnSiteMovieBaseVo> getSiteMovieByActor(Long sid, Long actorId, final String sortType, Integer sortOrder, Integer currPage, Integer pageSize) {
        LambdaQueryChainWrapper<KpnSiteMovie> lambdaQueryChainWrapper = this.lambdaQuery();
        lambdaQueryChainWrapper.select(KpnSiteMovie::getMovieId);
        lambdaQueryChainWrapper.eq(KpnSiteMovie::getSiteId, sid);
        lambdaQueryChainWrapper.eq(KpnSiteMovie::getActorId, actorId);

        if (sortType.equalsIgnoreCase(KpnMovieSortTypeEnum.HOT.getType())) {
            lambdaQueryChainWrapper.orderBy(true, KpnSortOrderEnum.isAsc(sortOrder), KpnSiteMovie::getVv);
        }
        if (sortType.equalsIgnoreCase(KpnMovieSortTypeEnum.LATEST.getType())) {
            lambdaQueryChainWrapper.orderBy(true, KpnSortOrderEnum.isAsc(sortOrder), KpnSiteMovie::getCreateTime);
        }
        if (sortType.equalsIgnoreCase(KpnMovieSortTypeEnum.TIME.getType())) {
            lambdaQueryChainWrapper.orderBy(true, KpnSortOrderEnum.isAsc(sortOrder), KpnSiteMovie::getDuration);
        }

        Page<KpnSiteMovie> kpnSiteMoviePage = lambdaQueryChainWrapper.page(new Page<>(currPage, pageSize));
        List<Long> movieIds = kpnSiteMoviePage.getRecords().stream().map(KpnSiteMovie::getMovieId).collect(Collectors.toList());

        return getSiteMovieByIds(sid, movieIds);
    }

    @Override
    public List<KpnSiteMovieBaseVo> searchSiteMovie(Long sid, String sortType, Integer sortOrder, Integer currPage, Integer pageSize) {
        LambdaQueryChainWrapper<KpnSiteMovie> lambdaQueryChainWrapper = this.lambdaQuery();
        lambdaQueryChainWrapper.select(KpnSiteMovie::getMovieId);
        lambdaQueryChainWrapper.eq(KpnSiteMovie::getSiteId, sid);
        lambdaQueryChainWrapper.eq(KpnSiteMovie::getStatus, SiteMovieStatusEnum.ON_SHELF.getStatus());
        lambdaQueryChainWrapper.eq(KpnSiteMovie::getPayType, true);

        if (sortType.equalsIgnoreCase(KpnMovieSortTypeEnum.HOT.getType())) {
            lambdaQueryChainWrapper.orderBy(true, KpnSortOrderEnum.isAsc(sortOrder), KpnSiteMovie::getVv);
        }
        if (sortType.equalsIgnoreCase(KpnMovieSortTypeEnum.LATEST.getType())) {
            lambdaQueryChainWrapper.orderBy(true, KpnSortOrderEnum.isAsc(sortOrder), KpnSiteMovie::getCreateTime);
        }
        if (sortType.equalsIgnoreCase(KpnMovieSortTypeEnum.TIME.getType())) {
            lambdaQueryChainWrapper.orderBy(true, KpnSortOrderEnum.isAsc(sortOrder), KpnSiteMovie::getDuration);
        }

        Page<KpnSiteMovie> kpnSiteMoviePage = lambdaQueryChainWrapper.page(new Page<>(currPage, pageSize));
        List<Long> movieIds = kpnSiteMoviePage.getRecords().stream().map(KpnSiteMovie::getMovieId).collect(Collectors.toList());

        return getSiteMovieByIds(sid, movieIds);
    }

    private void cacheSiteMovieVv(String siteMovieVvKey, Long sid, Long movieId) {
        Object siteMovieVvObj = RedisRepository.get(siteMovieVvKey);

        if (ObjectUtil.isEmpty(siteMovieVvObj)) {
            String lockKey = StrUtil.format("Lock:" + siteMovieVvKey, sid, movieId);
            boolean lockedSuccess = RedissLockUtil.tryLock(lockKey, PornConstants.Lock.WAIT_TIME, PornConstants.Lock.LEASE_TIME);
            if (!lockedSuccess) {
                throw new RuntimeException("加锁失败");
            }
            try {
                siteMovieVvObj = RedisRepository.get(siteMovieVvKey);
                if (ObjectUtil.isEmpty(siteMovieVvObj)) {
                    Long vv = this.lambdaQuery()
                            .eq(KpnSiteMovie::getSiteId, sid)
                            .eq(KpnSiteMovie::getMovieId, movieId)
                            .one().getVv();

                    RedisRepository.setExpire(siteMovieVvKey, vv, PornConstants.RedisKey.EXPIRE_TIME_30_DAYS, TimeUnit.SECONDS);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                RedissLockUtil.unlock(lockKey);
            }
        }
    }

    private void cacheSiteMovieFavorites(String siteMovieFavoritesKey, Long sid, Long movieId) {
        Object siteMovieFavoritesObj = RedisRepository.get(siteMovieFavoritesKey);

        if (ObjectUtil.isEmpty(siteMovieFavoritesObj)) {
            String lockKey = StrUtil.format("Lock:" + siteMovieFavoritesKey, sid, movieId);
            boolean lockedSuccess = RedissLockUtil.tryLock(lockKey, PornConstants.Lock.WAIT_TIME, PornConstants.Lock.LEASE_TIME);
            if (!lockedSuccess) {
                throw new RuntimeException("加锁失败");
            }
            try {
                siteMovieFavoritesObj = RedisRepository.get(siteMovieFavoritesKey);
                if (ObjectUtil.isEmpty(siteMovieFavoritesObj)) {
                    Long favorites = this.lambdaQuery()
                            .eq(KpnSiteMovie::getSiteId, sid)
                            .eq(KpnSiteMovie::getMovieId, movieId)
                            .one().getFavorites();

                    RedisRepository.setExpire(siteMovieFavoritesKey, favorites, PornConstants.RedisKey.EXPIRE_TIME_30_DAYS, TimeUnit.SECONDS);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                RedissLockUtil.unlock(lockKey);
            }
        }
    }
}

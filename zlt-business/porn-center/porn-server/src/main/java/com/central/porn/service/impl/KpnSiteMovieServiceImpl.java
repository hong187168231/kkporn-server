package com.central.porn.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.common.constant.PornConstants;
import com.central.common.model.KpnMovie;
import com.central.common.model.KpnSiteMovie;
import com.central.common.model.KpnSiteUserMovieFavorites;
import com.central.common.model.enums.SiteMovieStatusEnum;
import com.central.common.redis.lock.RedissLockUtil;
import com.central.common.redis.template.RedisRepository;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.core.language.LanguageUtil;
import com.central.porn.entity.PornPageResult;
import com.central.porn.entity.co.MovieSearchParamCo;
import com.central.porn.entity.vo.KpnMovieVo;
import com.central.porn.entity.vo.KpnSiteMovieBaseVo;
import com.central.porn.entity.vo.KpnTagVo;
import com.central.porn.enums.KpnMovieSortTypeEnum;
import com.central.porn.enums.KpnSiteMovieSearchFromEnum;
import com.central.porn.enums.KpnSortOrderEnum;
import com.central.porn.mapper.KpnSiteMovieMapper;
import com.central.porn.service.*;
import com.central.porn.utils.PornUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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
    private IRptSiteMovieDateService rptSiteMovieDateService;

    @Autowired
    private IKpnSiteUserMovieFavoritesService siteUserMovieFavoritesService;

    @Autowired
    private IRptSiteSearchDateService siteSearchDateService;

    @Autowired
    private IRptSiteSearchTotalService siteSearchTotalService;

    @Autowired
    private TaskExecutor taskExecutor;

    @Override
    public List<KpnSiteMovieBaseVo> getSiteMovieByIds(Long sid, List<Long> movieIds, Boolean isDetail) {
        if (CollectionUtil.isEmpty(movieIds)) {
            return new ArrayList<>();
        }
        List<String> redisKeyVoList = movieIds.stream().map(movieId -> StrUtil.format(PornConstants.RedisKey.KPN_MOVIEID_VO_KEY, movieId)).collect(Collectors.toList());
        List<KpnMovieVo> cachedKpnMovieVos = (ArrayList) RedisRepository.mget(redisKeyVoList);
        boolean hasNullVoElem = cachedKpnMovieVos.stream().anyMatch(Objects::isNull);
        if (hasNullVoElem) {
            cachedKpnMovieVos = new ArrayList<>();

            List<KpnMovie> kpnMovies = movieService.lambdaQuery()
                    .eq(KpnMovie::getHandleStatus, true)
                    .eq(KpnMovie::getStatus, true)
                    .in(KpnMovie::getId, movieIds)
                    .list();

            for (KpnMovie kpnMovie : kpnMovies) {
                if (ObjectUtil.isNotEmpty(kpnMovie)) {
                    KpnMovieVo kpnMovieVo = new KpnMovieVo();
                    BeanUtil.copyProperties(kpnMovie, kpnMovieVo);
                    Boolean payType = this.lambdaQuery().select(KpnSiteMovie::getPayType).eq(KpnSiteMovie::getSiteId, sid).eq(KpnSiteMovie::getMovieId, kpnMovie.getId()).one().getPayType();
                    kpnMovieVo.setPayType(payType);

                    //获取标签信息
                    List<KpnTagVo> kpnTagVos = movieTagService.getTagByMovieId(kpnMovie.getId());
                    kpnMovieVo.setTagVos(kpnTagVos);

                    //获取播放量/收藏量
                    KpnSiteMovie siteMovie = siteMovieService.getSiteMovie(sid, kpnMovie.getId());
                    kpnMovieVo.setVv(siteMovie.getVv());
                    kpnMovieVo.setFavorites(siteMovie.getFavorites());

                    cachedKpnMovieVos.add(kpnMovieVo);
                }
            }

            if (CollectionUtil.isNotEmpty(cachedKpnMovieVos)) {
                for (KpnMovieVo kpnMovieVo : cachedKpnMovieVos) {
                    RedisRepository.setExpire(StrUtil.format(PornConstants.RedisKey.KPN_MOVIEID_VO_KEY, kpnMovieVo.getId()), kpnMovieVo, PornConstants.RedisKey.EXPIRE_TIME_30_DAYS);
                }
            }
        }
        long start = System.currentTimeMillis();
        List<KpnSiteMovieBaseVo> result = new ArrayList<>();
        for (KpnMovieVo kpnMovieVo : cachedKpnMovieVos) {
            if (!isDetail) {
                KpnSiteMovieBaseVo siteMovieBaseVo = new KpnSiteMovieBaseVo();
                BeanUtil.copyProperties(kpnMovieVo, siteMovieBaseVo);
                siteMovieBaseVo.setName(LanguageUtil.getLanguageName(siteMovieBaseVo));

                for (KpnTagVo tagVo : kpnMovieVo.getTagVos()) {
                    tagVo.setName(LanguageUtil.getLanguageName(tagVo));
                }
                result.add(siteMovieBaseVo);
            } else {
                result.add(kpnMovieVo);
            }
        }
        System.out.println("耗时22:" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public KpnMovieVo getSiteMovieDetail(Long sid, Long movieId) {
        List<KpnSiteMovieBaseVo> siteMovieVos = new ArrayList<>();

        //从缓存中查询影片信息
        String movieRedisKey = StrUtil.format(PornConstants.RedisKey.KPN_MOVIEID_VO_KEY, movieId);
        KpnMovieVo kpnMovieVo = (KpnMovieVo) RedisRepository.get(movieRedisKey);

        //缓存影片信息
        if (ObjectUtil.isEmpty(kpnMovieVo)) {
            kpnMovieVo = (KpnMovieVo)this.getSiteMovieByIds(sid, Collections.singletonList(movieId), true).get(0);
        }

        //处理多语言
        kpnMovieVo.setName(LanguageUtil.getLanguageName(kpnMovieVo));
        for (KpnTagVo tagVo : kpnMovieVo.getTagVos()) {
            tagVo.setName(LanguageUtil.getLanguageName(tagVo));
        }
        return kpnMovieVo;
    }

    @Override
    public KpnSiteMovie getSiteMovie(Long sid, Long movieId) {
        //先缓存
        String siteMovieVvKey = StrUtil.format(PornConstants.RedisKey.KPN_SITE_MOVIE_VV_KEY, sid, movieId);
        cacheSiteMovieVv(siteMovieVvKey, sid, movieId);
        long vv = RedisRepository.incr(siteMovieVvKey);

        String siteMovieFavoritesKey = StrUtil.format(PornConstants.RedisKey.KPN_SITE_MOVIE_FAVORITES_KEY, sid, movieId);
        cacheSiteMovieFavorites(siteMovieFavoritesKey, sid, movieId);
        long favorites = RedisRepository.incr(siteMovieFavoritesKey);


        taskExecutor.execute(() -> {
            RedisRepository.decr(siteMovieVvKey);
            RedisRepository.decr(siteMovieFavoritesKey);
        });

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
        if (sortType.equalsIgnoreCase(KpnMovieSortTypeEnum.DURATION.getType())) {
            lambdaQueryChainWrapper.orderBy(true, KpnSortOrderEnum.isAsc(sortOrder), KpnSiteMovie::getDuration);
        }

        Page<KpnSiteMovie> kpnSiteMoviePage = lambdaQueryChainWrapper.page(new Page<>(currPage, pageSize));
        List<Long> movieIds = kpnSiteMoviePage.getRecords().stream().map(KpnSiteMovie::getMovieId).collect(Collectors.toList());

        return getSiteMovieByIds(sid, movieIds, false);
    }

    @Override
    public List<KpnSiteMovieBaseVo> searchSiteMovie(Long sid, MovieSearchParamCo searchParam, String sortType, Integer sortOrder, Integer currPage, Integer pageSize) {
        LambdaQueryChainWrapper<KpnSiteMovie> lambdaQueryChainWrapper = this.lambdaQuery();
        lambdaQueryChainWrapper.select(KpnSiteMovie::getMovieId);
        lambdaQueryChainWrapper.eq(KpnSiteMovie::getSiteId, sid);
        lambdaQueryChainWrapper.eq(KpnSiteMovie::getStatus, SiteMovieStatusEnum.ON_SHELF.getStatus());
        if (ObjectUtil.isNotEmpty(searchParam.getPayType())) {
            lambdaQueryChainWrapper.eq(KpnSiteMovie::getPayType, searchParam.getPayType());
        }

        if (sortType.equalsIgnoreCase(KpnMovieSortTypeEnum.HOT.getType())) {
            lambdaQueryChainWrapper.orderBy(true, KpnSortOrderEnum.isAsc(sortOrder), KpnSiteMovie::getVv);
        }
        if (sortType.equalsIgnoreCase(KpnMovieSortTypeEnum.LATEST.getType())) {
            lambdaQueryChainWrapper.orderBy(true, KpnSortOrderEnum.isAsc(sortOrder), KpnSiteMovie::getCreateTime);
        }
        if (sortType.equalsIgnoreCase(KpnMovieSortTypeEnum.DURATION.getType())) {
            lambdaQueryChainWrapper.orderBy(true, KpnSortOrderEnum.isAsc(sortOrder), KpnSiteMovie::getDuration);
        }

        Page<KpnSiteMovie> kpnSiteMoviePage = lambdaQueryChainWrapper.page(new Page<>(currPage, pageSize));
        List<Long> movieIds = kpnSiteMoviePage.getRecords().stream().map(KpnSiteMovie::getMovieId).collect(Collectors.toList());

        return getSiteMovieByIds(sid, movieIds, false);
    }

    @Override
    public List<KpnSiteMovieBaseVo> searchSiteMovieKeywords(Long sid, String keywords) {
        List<Long> movieIds = PornUtil.searchByKeywords(sid, keywords);

        List<KpnSiteMovieBaseVo> siteMovieBaseVos = getSiteMovieByIds(sid, movieIds, false);
        siteMovieBaseVos.sort(KpnSiteMovieBaseVo::compareByVv);

        if (CollectionUtil.isNotEmpty(movieIds)) {
            siteSearchDateService.saveRptSiteSearchDateNumber(sid, keywords);
            siteSearchTotalService.saveRptSiteSearchTotalNumber(sid, keywords);

        }
        return siteMovieBaseVos;
    }

    @Override
    public PornPageResult<KpnSiteMovieBaseVo> searchDepot(Long sid, Integer from, Long fromId, String sortType, Integer sortOrder, Integer currPage, Integer pageSize) {
        Page<KpnSiteMovie> page = new Page<>(currPage, pageSize);

        List<Long> movieIds = new ArrayList<>();
        Long total = 0L;
        //找片
        if (KpnSiteMovieSearchFromEnum.SEARCH.getCode().equals(from)) {
            LambdaQueryWrapper<KpnSiteMovie> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(KpnSiteMovie::getSiteId, sid);
            wrapper.orderByDesc(KpnSiteMovie::getVv);

            Page<KpnSiteMovie> list = baseMapper.selectPage(page, wrapper);
            total = page.getTotal();
            movieIds = list.getRecords().stream().map(KpnSiteMovie::getMovieId).collect(Collectors.toList());
        }
        //标签
        else if (KpnSiteMovieSearchFromEnum.TAG.getCode().equals(from)) {
            //最热 默认
            String redisKey = StrUtil.format(PornConstants.RedisKey.KPN_SITE_TAG_MOVIEID_VV, fromId);
            //最新
            if (sortType.equalsIgnoreCase(KpnMovieSortTypeEnum.LATEST.getType())) {
                redisKey = StrUtil.format(PornConstants.RedisKey.KPN_SITE_TAG_MOVIEID_CREATETIME, fromId);
            }
            //时长
            else if (sortType.equalsIgnoreCase(KpnMovieSortTypeEnum.DURATION.getType())) {
                redisKey = StrUtil.format(PornConstants.RedisKey.KPN_SITE_TAG_MOVIEID_DURATION, fromId);
            }

            //倒序(默认)
            int startIndex = (currPage - 1) * pageSize;
            int endIndex = startIndex + (pageSize - 1);
            //正序
            if (KpnSortOrderEnum.isAsc(sortOrder)) {
                endIndex = -(pageSize * (currPage - 1)) + (-1);
                startIndex = endIndex + (-(pageSize-1));
            }

            List<String> movieIdStrs = (ArrayList) RedisRepository.getList(redisKey, startIndex, endIndex);
            boolean hasNullVoElem = movieIdStrs.stream().anyMatch(Objects::isNull);
            if (!hasNullVoElem) {
                movieIds = movieIdStrs.stream().map(Long::valueOf).collect(Collectors.toList());
                //正序
                if (KpnSortOrderEnum.isAsc(sortOrder)) {
                    CollectionUtil.reverse(movieIds);
                }
            }
            total = RedisRepository.length(redisKey);
        }
        Integer totalPage = (int) (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
        List<KpnSiteMovieBaseVo> KpnSiteMovieBaseVos = this.getSiteMovieByIds(sid, movieIds, false);

        return PornPageResult.<KpnSiteMovieBaseVo>builder()
                .data(KpnSiteMovieBaseVos)
                .currPage(currPage)
                .pageSize(pageSize)
                .count(total)
                .totalPage(totalPage)
                .build();
    }

    @Override
    public List<Long> getSiteMovieIds(Long sid) {
        return this.lambdaQuery().eq(KpnSiteMovie::getSiteId, sid).eq(KpnSiteMovie::getStatus, true)
                .list()
                .stream().map(KpnSiteMovie::getMovieId).collect(Collectors.toList());
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

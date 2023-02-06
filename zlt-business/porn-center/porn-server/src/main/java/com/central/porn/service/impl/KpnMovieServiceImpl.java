package com.central.porn.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.central.common.constant.PornConstants;
import com.central.common.model.KpnMovie;
import com.central.common.model.KpnSiteMovie;
import com.central.common.model.enums.SiteMovieStatusEnum;
import com.central.common.redis.template.RedisRepository;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.entity.vo.KpnMovieBaseVo;
import com.central.porn.entity.vo.KpnMovieVo;
import com.central.porn.entity.vo.KpnTagVo;
import com.central.porn.mapper.KpnMovieMapper;
import com.central.porn.service.IKpnMovieService;
import com.central.porn.service.IKpnMovieTagService;
import com.central.porn.service.IKpnSiteMovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class KpnMovieServiceImpl extends SuperServiceImpl<KpnMovieMapper, KpnMovie> implements IKpnMovieService {

    @Autowired
    private IKpnMovieTagService movieTagService;

    @Override
    public List<KpnMovieBaseVo> getMovieByIds(List<Long> movieIds) {
        List<KpnMovieBaseVo> kpnMovieVos = new ArrayList<>();

        //从缓存中获取影片信息
        for (Long movieId : movieIds) {
            String movieRedisKey = StrUtil.format(PornConstants.RedisKey.KPN_MOVIEID_KEY, movieId);
            KpnMovie kpnMovie = (KpnMovie) RedisRepository.get(movieRedisKey);

            //查库重新缓存
            if (ObjectUtil.isEmpty(kpnMovie)) {
                kpnMovie = this.lambdaQuery()
                        .eq(KpnMovie::getHandleStatus, true)
                        .eq(KpnMovie::getStatus, true)
                        .in(KpnMovie::getId, movieId)
                        .one();
                if (ObjectUtil.isNotEmpty(kpnMovie)) {
                    RedisRepository.set(movieRedisKey, kpnMovie);
                }
            }

            if (ObjectUtil.isNotEmpty(kpnMovie)) {
                KpnMovieBaseVo kpnMovieBaseVo = new KpnMovieBaseVo();
                BeanUtil.copyProperties(kpnMovie, kpnMovieBaseVo);

                //获取标签信息
                List<KpnTagVo> kpnTagVos = movieTagService.getTagByMovieId(kpnMovie.getId());
                kpnMovieBaseVo.setTagVos(kpnTagVos);
                kpnMovieVos.add(kpnMovieBaseVo);
            }
        }
        return kpnMovieVos;
    }

    @Async
    @Override
    public void addMovieVv(Long movieId) {
        this.lambdaUpdate()
                .eq(KpnMovie::getId, movieId)
                .setSql(" `vv` = `vv` + 1")
                .update();
    }

    @Async
    @Override
    public void addMovieFavorites(Long movieId) {
        this.lambdaUpdate()
                .eq(KpnMovie::getId, movieId)
                .setSql(" `favorites` = `favorites` + 1")
                .update();
    }
}

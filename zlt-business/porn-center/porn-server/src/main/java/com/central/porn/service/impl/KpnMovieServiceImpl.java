package com.central.porn.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.central.common.constant.PornConstants;
import com.central.common.model.KpnMovie;
import com.central.common.redis.template.RedisRepository;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.entity.vo.KpnMovieVo;
import com.central.porn.entity.vo.KpnTagVo;
import com.central.porn.mapper.KpnMovieMapper;
import com.central.porn.service.IKpnMovieService;
import com.central.porn.service.IKpnMovieTagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class KpnMovieServiceImpl extends SuperServiceImpl<KpnMovieMapper, KpnMovie> implements IKpnMovieService {

    @Autowired
    private IKpnMovieTagService movieTagService;

    @Override
    public List<KpnMovieVo> getMovieByIds(List<Long> movieIds) {
        List<KpnMovieVo> kpnMovieVos = new ArrayList<>();

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
                KpnMovieVo kpnMovieVo = new KpnMovieVo();
                BeanUtil.copyProperties(kpnMovie, kpnMovieVo);

                //获取标签信息
                List<KpnTagVo> kpnTagVos = movieTagService.getTagByMovieId(kpnMovie.getId());
                kpnMovieVo.setTagVos(kpnTagVos);
                kpnMovieVos.add(kpnMovieVo);
            }
        }
        return kpnMovieVos;
    }
}

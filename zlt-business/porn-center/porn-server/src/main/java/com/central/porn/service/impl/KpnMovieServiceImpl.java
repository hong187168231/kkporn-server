package com.central.porn.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.central.common.constant.PornConstants;
import com.central.common.model.KpnMovie;
import com.central.common.model.KpnSiteMovie;
import com.central.common.redis.template.RedisRepository;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.entity.vo.KpnSiteMovieBaseVo;
import com.central.porn.entity.vo.KpnTagVo;
import com.central.porn.mapper.KpnMovieMapper;
import com.central.porn.service.IKpnMovieService;
import com.central.porn.service.IKpnMovieTagService;
import com.central.porn.service.IKpnSiteMovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class KpnMovieServiceImpl extends SuperServiceImpl<KpnMovieMapper, KpnMovie> implements IKpnMovieService {

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

    @Async
    @Override
    public void removeMovieFavorites(Long movieId) {
        this.lambdaUpdate()
                .eq(KpnMovie::getId, movieId)
                .setSql(" `favorites` = `favorites` - 1")
                .update();
    }
}

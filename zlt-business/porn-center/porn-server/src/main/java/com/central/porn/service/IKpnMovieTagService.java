package com.central.porn.service;

import com.central.common.KpnMovieTag;
import com.central.common.model.KpnMovie;
import com.central.common.service.ISuperService;
import com.central.porn.entity.vo.KpnTagVo;

import java.util.List;


public interface IKpnMovieTagService extends ISuperService<KpnMovieTag> {
    /**
     * 获取电影标签
     *
     * @param movieId 电影id
     * @return
     */
    List<KpnTagVo> getTagByMovieId(Long movieId);
}

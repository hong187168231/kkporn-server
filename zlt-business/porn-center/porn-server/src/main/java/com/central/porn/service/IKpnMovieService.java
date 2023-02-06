package com.central.porn.service;

import com.central.common.model.KpnMovie;
import com.central.common.service.ISuperService;
import com.central.porn.entity.vo.KpnMovieVo;

import java.util.List;


public interface IKpnMovieService extends ISuperService<KpnMovie> {
    /**
     * 获取影片信息
     *
     * @param movieIds 影片id
     * @return
     */
    List<KpnMovieVo> getMovieByIds(List<Long> movieIds);
}

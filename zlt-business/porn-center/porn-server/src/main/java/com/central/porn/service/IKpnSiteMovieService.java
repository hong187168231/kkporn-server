package com.central.porn.service;

import com.central.common.model.KpnSiteMovie;
import com.central.common.service.ISuperService;


public interface IKpnSiteMovieService extends ISuperService<KpnSiteMovie> {
    /**
     * 增加站点影片播放量
     *
     * @param sid  站点id
     * @param movieId 影片id
     */
    Long addSiteMovieVv(Long sid, Long movieId);
}

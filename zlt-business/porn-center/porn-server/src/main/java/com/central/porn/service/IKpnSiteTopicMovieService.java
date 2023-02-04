package com.central.porn.service;

import com.central.common.model.KpnSiteTopicMovie;
import com.central.common.service.ISuperService;

import java.util.List;


public interface IKpnSiteTopicMovieService extends ISuperService<KpnSiteTopicMovie> {

    /**
     * 获取专题最新5部影片
     *
     * @param sid      站点id
     * @param topicId  专题id
     * @param currPage 当前页
     * @param size     每页条数
     * @return
     */
    List<Long> getMovieIds(Long sid, Long topicId, Integer currPage, Integer size);
}

package com.central.backend.service;

import com.central.backend.model.dto.KpnSiteMoviePayTypeDto;
import com.central.backend.model.dto.KpnSiteMovieStatusDto;
import com.central.backend.model.vo.KpnSiteMovieVO;
import com.central.backend.vo.MovieVo;
import com.central.common.model.KpnSiteMovie;
import com.central.common.model.PageResult;
import com.central.common.model.SysUser;
import com.central.common.service.ISuperService;

import java.util.List;
import java.util.Map;

/**
 * 站点影片
 *
 * @author yixiu
 * @date 2023-02-20 17:00:56
 */
public interface IKpnSiteMovieService extends ISuperService<KpnSiteMovie> {
    /**
     * 列表
     * @param params
     * @return
     */
    PageResult<KpnSiteMovieVO> findList(Map<String, Object> params, SysUser user);

    public void updateBatchStatusById(List<KpnSiteMovieStatusDto> list,SysUser user);

    public void updateBatchPayTypeById(List<KpnSiteMoviePayTypeDto> list, SysUser user);


    /**
     * 根据站点查询影片列表
     * @param params
     * @return
     */
    PageResult<MovieVo> findMovieList(Map<String, Object> params);

}


package com.central.backend.service;

import com.central.backend.model.vo.KpnTagVO;
import com.central.common.model.KpnTag;
import com.central.common.model.PageResult;
import com.central.common.service.ISuperService;

import java.util.Map;

/**
 * 影片标签
 *
 * @author yixiu
 * @date 2023-02-03 16:32:41
 */
public interface IKpnTagService extends ISuperService<KpnTag> {
    /**
     * 列表
     *
     * @param params
     * @return
     */
    PageResult<KpnTagVO> findList(Map<String, Object> params);
}


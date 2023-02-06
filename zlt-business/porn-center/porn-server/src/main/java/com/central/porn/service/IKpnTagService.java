package com.central.porn.service;

import com.central.common.model.KpnTag;
import com.central.common.service.ISuperService;
import com.central.porn.entity.vo.KpnTagVo;


public interface IKpnTagService extends ISuperService<KpnTag> {
    /**
     * 查询标签信息
     * @param tagId 标签id
     * @return
     */
    KpnTagVo getByTagId(Long tagId);
}

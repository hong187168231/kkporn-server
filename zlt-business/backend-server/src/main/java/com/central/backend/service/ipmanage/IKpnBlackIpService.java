package com.central.backend.service.ipmanage;

import com.central.common.model.PageResult;
import com.central.common.model.ipmanage.KpnBlackIp;
import com.central.common.service.ISuperService;

import java.util.Map;

/**
 * 
 *
 * @author yixiu
 * @date 2023-02-03 15:50:11
 */
public interface IKpnBlackIpService extends ISuperService<KpnBlackIp> {
    /**
     * 列表
     * @param params
     * @return
     */
    PageResult<KpnBlackIp> findList(Map<String, Object> params);
}


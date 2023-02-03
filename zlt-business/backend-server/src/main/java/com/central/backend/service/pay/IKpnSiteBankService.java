package com.central.backend.service.pay;

import com.central.common.model.PageResult;
import com.central.common.model.pay.KpnSiteBank;
import com.central.common.service.ISuperService;

import java.util.Map;

/**
 * 收款银行渠道
 *
 * @author yixiu
 * @date 2023-02-03 19:35:22
 */
public interface IKpnSiteBankService extends ISuperService<KpnSiteBank> {
    /**
     * 列表
     * @param params
     * @return
     */
    PageResult<KpnSiteBank> findList(Map<String, Object> params);
}


package com.central.backend.service.pay;

import com.central.common.model.PageResult;
import com.central.common.model.pay.KpnSiteBankCard;
import com.central.common.service.ISuperService;

import java.util.Map;

/**
 * 收款银行卡配置
 *
 * @author yixiu
 * @date 2023-02-03 19:35:22
 */
public interface IKpnSiteBankCardService extends ISuperService<KpnSiteBankCard> {
    /**
     * 列表
     * @param params
     * @return
     */
    PageResult<KpnSiteBankCard> findList(Map<String, Object> params);
}


package com.central.backend.service.pay;

import com.central.common.model.PageResult;
import com.central.common.model.Result;
import com.central.common.model.SysUser;
import com.central.common.model.pay.KpnSiteProduct;
import com.central.common.service.ISuperService;

import java.util.Map;

/**
 * vip产品
 *
 * @author yixiu
 * @date 2023-02-03 19:35:22
 */
public interface IKpnSiteProductService extends ISuperService<KpnSiteProduct> {
    /**
     * 列表
     * @param params
     * @return
     */
    PageResult<KpnSiteProduct> findList(Map<String, Object> params, SysUser user);
    Result saveOrUpdateKpnSiteProduct(KpnSiteProduct kpnSiteProduct, SysUser user);
}


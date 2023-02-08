package com.central.backend.service;

import com.central.backend.co.KpnMoneyLogCo;
import com.central.common.model.KpnMoneyLog;
import com.central.common.model.PageResult;
import com.central.common.service.ISuperService;


/*
 * @Author: Lulu
 * @Date: 2023/2/8
 */
public interface IKpnMoneyLogService extends ISuperService<KpnMoneyLog> {

     PageResult<KpnMoneyLog> findMoneyLogList(KpnMoneyLogCo params);
}

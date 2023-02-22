package com.central.porn.service;

import com.central.common.model.KpnMoneyLog;
import com.central.common.model.SysUser;
import com.central.common.service.ISuperService;

import java.math.BigDecimal;

public interface IKpnMoneyLogService extends ISuperService<KpnMoneyLog> {

     /**
      * 保存K币账变
      *
      * @param sysUser  会员
      * @param type     账变类型
      * @param rewardKb 账变
      */
     void addKbChangeLog(SysUser sysUser, Integer type, BigDecimal rewardKb);

}

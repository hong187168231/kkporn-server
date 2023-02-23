package com.central.porn.service;

import com.central.common.model.KpnMoneyLog;
import com.central.common.model.SysUser;
import com.central.common.service.ISuperService;

import java.math.BigDecimal;
import java.util.Map;

public interface IKpnMoneyLogService extends ISuperService<KpnMoneyLog> {

    /**
     * 保存K币账变
     *
     * @param sysUser  会员
     * @param type     账变类型
     * @param rewardKb 账变
     * @param params   其他参数
     */
    void addKbChangeLog(SysUser sysUser, Integer type, BigDecimal rewardKb, Map<String, Object> params);

    /**
     * 获取推广累计总kb奖励
     *
     * @param userId 会员id
     * @return
     */
    BigDecimal getPromotionRewardTotalKbs(Long userId);

    /**
     * 获取推广人今日总推广奖励K币金额
     *
     * @param userId 推广人id
     * @return
     */
    BigDecimal getUserTodayPromoteTotalKbs(Long userId);
}

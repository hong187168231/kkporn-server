package com.central.porn.service;

import com.central.common.model.SysUser;
import com.central.common.service.ISuperService;

import java.math.BigDecimal;

/**
 * @author zlt
 * <p>
 * Blog: https://zlt2000.gitee.io
 * Github: https://github.com/zlt2000
 */
public interface ISysUserService extends ISuperService<SysUser> {

    /**
     * 根据邀请码获取邀请人
     *
     * @param inviteCode 邀请码
     * @return
     */
    SysUser getByInviteCode(String inviteCode);

    /**
     * 增加奖励vip天数
     *
     * @param sysUser 登录会员
     * @param vipDays 新增vip天数
     */
    void addRewardVipDays(SysUser sysUser, Integer vipDays);

    /**
     * 增加奖励K币数
     *
     * @param sysUser  登录会员
     * @param rewardKb 奖励K币数
     */
    void addRewardKb(SysUser sysUser, BigDecimal rewardKb);
}

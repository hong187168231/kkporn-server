package com.central.porn.service.impl;

import cn.hutool.core.date.DateUtil;
import com.central.common.model.SysUser;
import com.central.common.model.enums.UserTypeEnum;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.mapper.SysUserMapper;
import com.central.porn.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * year
 */
@Slf4j
@Service
public class SysUserServiceImpl extends SuperServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Override
    public SysUser getByInviteCode(String inviteCode) {
        return this.lambdaQuery()
                .eq(SysUser::getType, UserTypeEnum.APP.name())
                .eq(SysUser::getPromotionCode, inviteCode)
                .one();
    }

    @Override
    public void addRewardVipDays(SysUser sysUser, Integer vipDays) {
        this.lambdaUpdate().eq(SysUser::getId, sysUser.getId())
                .set(SysUser::getVipExpire, sysUser.getVipExpire() == null ? DateUtil.offsetDay(new Date(), vipDays) : DateUtil.offsetDay(sysUser.getVipExpire(), vipDays))
                .set(SysUser::getVip, true)
                .update();
    }

    @Override
    public void addRewardKb(SysUser sysUser, BigDecimal rewardKb) {
        this.lambdaUpdate().eq(SysUser::getId, sysUser.getId())
                .setSql( "`k_balance` = `k_balance` + "+rewardKb)
                .update();
    }
}


























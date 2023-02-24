package com.central.porn.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.central.common.model.KpnMoneyLog;
import com.central.common.model.SysUser;
import com.central.common.model.enums.KbChangeTypeEnum;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.mapper.KpnMoneyLogMapper;
import com.central.porn.service.IKpnMoneyLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;


@Slf4j
@Service
public class KpnMoneyLogServiceImpl extends SuperServiceImpl<KpnMoneyLogMapper, KpnMoneyLog> implements IKpnMoneyLogService {

    @Override
    public void addKbChangeLog(SysUser sysUser, Integer type, BigDecimal rewardKb, Map<String, Object> params) {
        KpnMoneyLog kpnMoneyLog = new KpnMoneyLog();
        kpnMoneyLog.setUserId(sysUser.getId());
        kpnMoneyLog.setUserName(sysUser.getUsername());
        kpnMoneyLog.setOrderNo(RandomUtil.randomNumbers(20));
        kpnMoneyLog.setOrderType(type);
        kpnMoneyLog.setBeforeMoney(sysUser.getKBalance());
        kpnMoneyLog.setMoney(rewardKb);
        kpnMoneyLog.setAfterMoney(sysUser.getKBalance().add(rewardKb));
        kpnMoneyLog.setDate(DateUtil.formatDate(new Date()));
        //签到获取奖励
        if (type.equals(KbChangeTypeEnum.SIGN_REWARD.getType())) {
            kpnMoneyLog.setRemark("签到奖励kb:" + rewardKb.toPlainString());
        }
        //填入邀请码获取奖励
        else if (type.equals(KbChangeTypeEnum.FILL_INVITE_CODE.getType())) {
            kpnMoneyLog.setRemark(StrUtil.format("填写邀请码:{},获取奖励K币:{}", params.get("inviteCode"), rewardKb.toPlainString()));
        }
        //推广获取奖励
        else if (type.equals(KbChangeTypeEnum.PROMOTION.getType())) {
            kpnMoneyLog.setRemark(StrUtil.format("推广会员id:{},获取奖励K币: {}", params.get("userId"), rewardKb.toPlainString()));
        }
        //购买vip
        else if (type.equals(KbChangeTypeEnum.OPEN_VIP.getType())) {
            kpnMoneyLog.setRemark(StrUtil.format("购买vip产品id:{},消费K币: {}", params.get("productId"), rewardKb.toPlainString()));
        }

        save(kpnMoneyLog);
    }

    @Override
    public BigDecimal getPromotionRewardTotalKbs(Long userId) {
        return this.baseMapper.getRewardKbsByKbChangeType(userId, KbChangeTypeEnum.PROMOTION.getType());
    }

    @Override
    public BigDecimal getUserTodayPromoteTotalKbs(Long userId) {
        String today = DateUtil.formatDate(new Date());
        Integer kbChangeTypeCode = KbChangeTypeEnum.PROMOTION.getType();
        return this.baseMapper.getUserTodayPromoteTotalKb(userId, today, kbChangeTypeCode);
    }
}




























package com.central.porn.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.central.common.model.KpnMoneyLog;
import com.central.common.model.SysUser;
import com.central.common.model.enums.KbChangeTypeEnum;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.mapper.KpnMoneyLogMapper;
import com.central.porn.service.IKpnMoneyLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Slf4j
@Service
public class KpnMoneyLogServiceImpl extends SuperServiceImpl<KpnMoneyLogMapper, KpnMoneyLog> implements IKpnMoneyLogService {

    @Override
    public void addKbChangeLog(SysUser sysUser, Integer type, BigDecimal rewardKb) {
        KpnMoneyLog kpnMoneyLog = new KpnMoneyLog();
        kpnMoneyLog.setUserId(sysUser.getId());
        kpnMoneyLog.setUserName(sysUser.getUsername());
        kpnMoneyLog.setOrderNo(RandomUtil.randomNumbers(20));
        kpnMoneyLog.setOrderType(type);
        kpnMoneyLog.setBeforeMoney(sysUser.getKBalance());
        kpnMoneyLog.setMoney(rewardKb);
        kpnMoneyLog.setAfterMoney(sysUser.getKBalance().add(rewardKb));
        //签到获取奖励
        if (type.equals(KbChangeTypeEnum.SIGN_REWARD.getType())) {
            kpnMoneyLog.setRemark("签到奖励kb:" + rewardKb.toPlainString());
        }

        save(kpnMoneyLog);
    }
}
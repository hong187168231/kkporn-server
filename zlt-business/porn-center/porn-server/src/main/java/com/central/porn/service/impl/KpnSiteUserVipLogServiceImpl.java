package com.central.porn.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.central.common.model.KpnSiteUserVipLog;
import com.central.common.model.SysUser;
import com.central.common.model.enums.VipChangeTypeEnum;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.mapper.KpnSiteUserVipLogMapper;
import com.central.porn.service.IKpnSiteUserVipLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class KpnSiteUserVipLogServiceImpl extends SuperServiceImpl<KpnSiteUserVipLogMapper, KpnSiteUserVipLog> implements IKpnSiteUserVipLogService {
    @Override
    public void addVipDaysChangeLog(SysUser sysUser, Integer changeTypeCode, Integer days, BigDecimal amount, String currencyCode, Map<String, Object> params) {
        KpnSiteUserVipLog siteUserVipLog = new KpnSiteUserVipLog();
        siteUserVipLog.setSiteId(sysUser.getSiteId());
        siteUserVipLog.setSiteCode(sysUser.getSiteCode());
        siteUserVipLog.setSiteName(sysUser.getSiteName());
        siteUserVipLog.setUserId(sysUser.getId());
        siteUserVipLog.setUsername(sysUser.getUsername());
        siteUserVipLog.setType(changeTypeCode);
        if (amount != null) {
            siteUserVipLog.setAmount(amount);
        }
        if (currencyCode != null) {
            siteUserVipLog.setCurrencyCode(currencyCode);
        }
        siteUserVipLog.setBeforeExpire(sysUser.getVipExpire());
        siteUserVipLog.setDays(days);
        siteUserVipLog.setAfterExpire(sysUser.getVipExpire() == null ? DateUtil.offsetDay(new Date(), days):DateUtil.offsetDay(sysUser.getVipExpire(), days));
        //签到
        if (changeTypeCode.equals(VipChangeTypeEnum.SIGN.getCode())) {
            String remark = StrUtil.format(VipChangeTypeEnum.getLogFormatByCode(changeTypeCode), params.get("signDays"), days);
            siteUserVipLog.setRemark(remark);
        }

        save(siteUserVipLog);
    }
}

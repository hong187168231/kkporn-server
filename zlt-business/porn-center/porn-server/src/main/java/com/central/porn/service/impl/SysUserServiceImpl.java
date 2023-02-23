package com.central.porn.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.central.common.constant.PornConstants;
import com.central.common.model.KpnSite;
import com.central.common.model.KpnSitePromotion;
import com.central.common.model.Result;
import com.central.common.model.SysUser;
import com.central.common.model.enums.UserRegTypeEnum;
import com.central.common.model.enums.UserTypeEnum;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.mapper.SysUserMapper;
import com.central.porn.service.IKpnSitePromotionService;
import com.central.porn.service.IKpnSiteService;
import com.central.porn.service.ISysUserService;
import com.central.user.feign.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .setSql("`k_balance` = `k_balance` + " + rewardKb)
                .update();
    }

    @Override
    public Integer getPromotionMemberCount(String promotionCode) {

        return this.lambdaQuery().eq(SysUser::getInviteCode, promotionCode).count();
    }

    @Autowired
    private IKpnSiteService siteService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IKpnSitePromotionService promotionConfigService;


    @Override
    @Transactional
    public void register(Long sid, SysUser promoteUser, String nickName, String username, String password) {
        KpnSite siteInfo = siteService.getInfoById(sid);

        SysUser newAppUser = new SysUser();
        newAppUser.setSiteId(sid);
        newAppUser.setSiteCode(siteInfo.getCode());
        newAppUser.setSiteName(siteInfo.getName());
        newAppUser.setUsername(username);
        newAppUser.setNickname(nickName);
        newAppUser.setType(UserTypeEnum.APP.name());
        newAppUser.setPassword(passwordEncoder.encode(password));
        newAppUser.setIsReg(UserRegTypeEnum.SELF_REG.getType());
        newAppUser.setKBalance(BigDecimal.ZERO);

        if (ObjectUtil.isNotEmpty(promoteUser)) {
            newAppUser.setParentId(promoteUser.getId());
            newAppUser.setParentName(promoteUser.getUsername());
            newAppUser.setInviteCode(promoteUser.getPromotionCode());
        }
        boolean succeed = false;
        int tryTimes = 1;
        do {
            String promotionCode = RandomUtil.randomString(PornConstants.Str.RANDOM_BASE_STR, PornConstants.Numeric.INVITE_CODE_LENGTH);
            try {
                newAppUser.setPromotionCode(promotionCode);
                succeed = sysUserService.save(newAppUser);
            } catch (Exception e) {
                log.error(promotionCode + " : " + e.getMessage(), e);
                if (tryTimes++ >= 3) {
                    throw e;
                }
            }
        } while (!succeed);

        KpnSitePromotion sitePromotionConfig = promotionConfigService.getBySiteId(sid);
        if (ObjectUtil.isEmpty(sitePromotionConfig)) {
            return ;
        }
        promotionConfigService.addPromotionDatas(sitePromotionConfig, newAppUser, promoteUser, promoteUser.getPromotionCode());
    }
}


























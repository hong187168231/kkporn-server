package com.central.porn.service.impl;

import com.central.common.model.SysUser;
import com.central.common.model.enums.UserTypeEnum;
import com.central.common.service.impl.SuperServiceImpl;
import com.central.porn.mapper.SysUserMapper;
import com.central.porn.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
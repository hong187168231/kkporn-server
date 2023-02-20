package com.central.porn.service;

import com.central.common.model.SysUser;
import com.central.common.service.ISuperService;

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
}

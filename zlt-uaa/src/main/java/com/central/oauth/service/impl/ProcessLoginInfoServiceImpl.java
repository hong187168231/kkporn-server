package com.central.oauth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.central.common.constant.CommonConstant;
import com.central.common.model.LoginLog;
import com.central.common.model.SysUser;
import com.central.oauth.service.ProcessLoginInfoService;
import com.central.user.feign.UserService;
import com.central.user.model.co.SysUserCo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
public class ProcessLoginInfoServiceImpl implements ProcessLoginInfoService {

    @Autowired
    private UserService userService;

    @Async
    @Override
    public void processLoginInfo(UserDetails userDetails, String loginIp) {
        log.info("+++++++ logInIp is {}", loginIp);
        SysUser user = (SysUser) userDetails;
        SysUserCo sysUser = new SysUserCo();
        BeanUtil.copyProperties(user, sysUser);
        sysUser.setLoginIp(loginIp);
        sysUser.setLogin(Boolean.TRUE);
        sysUser.setOnlineStatus(CommonConstant.ONLINE);
        sysUser.setLastLoginTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        log.info("+++++++ sysUser {}", sysUser);
        userService.saveOrUpdate(sysUser);
        if (StrUtil.isNotBlank(loginIp)) {
            addLoginLog(user, loginIp);
        }
    }

    private void addLoginLog(SysUser sysUser, String loginIp) {
        LoginLog loginLog = new LoginLog();
        loginLog.setLoginIp(loginIp);
        loginLog.setLoginTime(DateTime.now());
        loginLog.setPlatName(sysUser.getUsername());
        loginLog.setUserId(sysUser.getId());
        loginLog.setType(sysUser.getType());
        loginLog.setNickName(sysUser.getNickname());
        if ((sysUser.getType().equals("BACKEND")) && CollUtil.isNotEmpty(sysUser.getRoles())) {
            loginLog.setRoleId(sysUser.getRoles().get(0).getId());
//            loginLog.setLoginIp("127.0.0.1");
        }
//        userService.addLoginlog(loginLog);
    }


}

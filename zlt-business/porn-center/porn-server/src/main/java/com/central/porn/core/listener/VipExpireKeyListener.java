package com.central.porn.core.listener;

import cn.hutool.core.util.StrUtil;
import com.central.common.constant.PornConstants;
import com.central.porn.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class VipExpireKeyListener implements MessageListener {

    @Autowired
    private ISysUserService sysUserService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] channel = message.getChannel();
        byte[] body = message.getBody();

        try {
            String p = new String(pattern, StandardCharsets.UTF_8);
            String title = new String(channel, StandardCharsets.UTF_8);
            String content = new String(body, StandardCharsets.UTF_8);

            if (content.startsWith(PornConstants.RedisKey.KPN_SITE_VIP_EXPIRE_PREFIX)) {
                log.info("key：" + title + ",event:" + content + ",p:" + p);
                long userId = Long.parseLong(StrUtil.subAfter(content,PornConstants.Symbol.COLON,true));
                sysUserService.updateVipExpire(userId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

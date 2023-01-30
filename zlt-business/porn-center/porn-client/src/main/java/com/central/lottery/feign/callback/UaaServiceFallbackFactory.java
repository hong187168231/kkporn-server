package com.central.lottery.feign.callback;

import com.central.common.model.Result;
import com.central.common.model.SysUser;
import com.central.lottery.feign.UaaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 降级工场
 */
@Slf4j
public class UaaServiceFallbackFactory implements FallbackFactory<UaaService> {

    @Override
    public UaaService create(Throwable cause) {
        return new UaaService() {
            @Override
            public Result<SysUser> getUserInfoByToken(@RequestParam("token") String token){
                log.error("查询失败:{}", token);
                return Result.failed("查询失败");
            }
        };
    }
}

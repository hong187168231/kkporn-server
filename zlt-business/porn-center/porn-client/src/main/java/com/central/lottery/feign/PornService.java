package com.central.lottery.feign;

import com.central.common.constant.ServiceNameConstants;
import com.central.lottery.feign.callback.PornServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

/**
 */
@FeignClient(name = ServiceNameConstants.PORN_SERVER,
        fallbackFactory = PornServiceFallbackFactory.class, decode404 = true)
public interface PornService {
}
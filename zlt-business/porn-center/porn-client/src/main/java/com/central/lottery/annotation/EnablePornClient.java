package com.central.lottery.annotation;

import com.central.lottery.feign.callback.PornServiceFallbackFactory;
import com.central.lottery.feign.callback.UaaServiceFallbackFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用配置服Feign接口
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableFeignClients(basePackages = "com.central")
@Import({PornServiceFallbackFactory.class, UaaServiceFallbackFactory.class})
public @interface EnablePornClient {

}

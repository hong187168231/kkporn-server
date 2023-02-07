package com.central.porn.config;

import com.central.common.config.DefaultWebMvcConfig;
import com.central.porn.core.interceptor.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
public class WebMvcConfig extends DefaultWebMvcConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor();
    }

    /**
     * 添加拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor()).addPathPatterns("/**");
    }
}

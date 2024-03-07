package io.github.liuziyuan.retrofit.spring.boot;

import io.github.liuziyuan.retrofit.spring.boot.RetrofitGlobalConfigProperties;
import io.github.liuziyuan.retrofit.spring.boot.RetrofitResourceDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 全局配置，随@EnableRetrofit 启动
 * @author liuziyuan
 */
@Configuration
public class RetrofitConfig {

    @Bean
    @ConditionalOnMissingBean
    public static RetrofitResourceDefinitionRegistry retrofitResourceDefinitionRegistry() {
        return new RetrofitResourceDefinitionRegistry();
    }

    @Bean
    @ConditionalOnMissingBean
    public RetrofitGlobalConfigProperties retrofitGlobalConfigProperties() {
        return new RetrofitGlobalConfigProperties();
    }
}

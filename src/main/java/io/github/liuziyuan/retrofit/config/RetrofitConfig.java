package io.github.liuziyuan.retrofit.config;

import io.github.liuziyuan.retrofit.RetrofitResourceDefinitionRegistry;
import io.github.liuziyuan.retrofit.extension.RetrofitCloudInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
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
    public RetrofitCloudInterceptor retrofitCloudInterceptor() {
        return new RetrofitCloudInterceptor();
    }
}

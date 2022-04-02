package io.github.liuziyuan.retrofit.config;

import io.github.liuziyuan.retrofit.RetrofitResourceDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuziyuan
 */
@Configuration
public class RetrofitConfig {

    private RetrofitConfig() {
    }

    @Bean
    @ConditionalOnMissingBean
    public static RetrofitResourceDefinitionRegistry retrofitResourceDefinitionRegistry() {
        return new RetrofitResourceDefinitionRegistry();
    }
}

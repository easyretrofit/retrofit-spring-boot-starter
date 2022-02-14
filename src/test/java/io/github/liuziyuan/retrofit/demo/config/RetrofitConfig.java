package io.github.liuziyuan.retrofit.demo.config;

import io.github.liuziyuan.retrofit.RetrofitResourceDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuziyuan
 */
@Configuration
public class RetrofitConfig {


    @Bean
    public RetrofitResourceDefinitionRegistry retrofitResourceDefinitionRegistry() {
        return new RetrofitResourceDefinitionRegistry();
    }


}

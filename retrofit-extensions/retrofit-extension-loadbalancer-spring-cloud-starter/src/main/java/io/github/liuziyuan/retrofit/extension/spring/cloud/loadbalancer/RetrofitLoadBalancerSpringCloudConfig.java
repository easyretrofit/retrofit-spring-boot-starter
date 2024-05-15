package io.github.liuziyuan.retrofit.extension.spring.cloud.loadbalancer;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RetrofitLoadBalancerSpringCloudConfig {
//    @Bean
//    @ConditionalOnMissingBean
//    public RetrofitLoadBalancerExtension retrofitSpringCouldWebConfig() {
//        return new RetrofitLoadBalancerExtension();
//    }

    @Bean
    @ConditionalOnMissingBean
    public RetrofitLoadBalancerInterceptor retrofitCloudInterceptor() {
        return new RetrofitLoadBalancerInterceptor();
    }
}

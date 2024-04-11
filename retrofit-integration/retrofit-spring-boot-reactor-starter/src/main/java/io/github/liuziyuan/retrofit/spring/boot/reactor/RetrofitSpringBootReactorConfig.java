package io.github.liuziyuan.retrofit.spring.boot.reactor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RetrofitSpringBootReactorConfig {
    @Bean
    @ConditionalOnMissingBean
    public CustomRetrofitBuilderExtension customRetrofitBuilderExtension() {
        return new CustomRetrofitBuilderExtension();
    }
}

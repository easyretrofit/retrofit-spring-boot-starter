package io.github.liuziyuan.retrofit.extension.sentinel.spring.boot.config;

import io.github.liuziyuan.retrofit.extension.sentinel.core.properties.RetrofitSentinelDegradeRuleProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "retrofit.sentinel.degrade")
public class RetrofitSpringSentinelDegradeRuleProperties extends RetrofitSentinelDegradeRuleProperties {
}

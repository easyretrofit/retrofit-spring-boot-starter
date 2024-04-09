package io.github.liuziyuan.retrofit.extension.sentinel.spring.boot.config;

import io.github.liuziyuan.retrofit.extension.sentinel.core.properties.RetrofitSentinelDegradeRuleProperties;
import io.github.liuziyuan.retrofit.extension.sentinel.core.properties.RetrofitSentinelFlowRuleProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "retrofit.sentinel.flow")
public class RetrofitSpringSentinelFlowRuleProperties extends RetrofitSentinelFlowRuleProperties {
}

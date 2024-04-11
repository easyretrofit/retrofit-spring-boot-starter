package io.github.liuziyuan.retrofit.extension.sentinel.core.properties;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import io.github.liuziyuan.retrofit.extension.sentinel.core.resource.CustomizeFlowRuleBean;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * RetrofitSentinelFlowRuleProperties configs have the same function as RetrofitSentinelFlowRule declared on a class <br>
 * The configs key need same name as RetrofitSentinelFlowRule resourceName<br>
 */
@Getter
@Setter
public class RetrofitSentinelFlowRuleProperties {

    private static final String DEFAULT = "default";

    private Map<String, InstanceProperties> instances = new HashMap<>();
    private Map<String, ConfigProperties> configs = new HashMap<>();

    @Getter
    @Setter
    public static class InstanceProperties extends CustomizeFlowRuleBean {
        private String baseConfig;
    }

    @Getter
    @Setter
    public static class ConfigProperties extends CustomizeFlowRuleBean {
    }

}

package io.github.liuziyuan.retrofit.extension.sentinel.core.properties;

import io.github.liuziyuan.retrofit.extension.sentinel.core.resource.CustomizeDegradeRuleBean;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * RetrofitSentinelGradeRuleProperties configs have the same function as RetrofitSentinelGradeRule declared on a class <br>
 * The configs key need same name as RetrofitSentinelDegradeRule resourceName<br>
 */
@Getter
@Setter
public class RetrofitSentinelDegradeRuleProperties {

    private static final String DEFAULT = "default";

    private Map<String, InstanceProperties> instances = new HashMap<>();
    private Map<String, ConfigProperties> configs = new HashMap<>();


    @Getter
    @Setter
    public static class InstanceProperties extends CustomizeDegradeRuleBean {
        private String baseConfig;
    }

    @Getter
    @Setter
    public static class ConfigProperties extends CustomizeDegradeRuleBean {
    }

}

package io.github.liuziyuan.retrofit.extension.sentinel.core.properties;

import io.github.liuziyuan.retrofit.extension.sentinel.core.resource.CustomizeDegradeRuleBean;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

import static io.github.liuziyuan.retrofit.extension.sentinel.core.AppConstants.DEFAULT;

/**
 * RetrofitSentinelGradeRuleProperties configs have the same function as RetrofitSentinelGradeRule declared on a class <br>
 * The configs key need same name as RetrofitSentinelDegradeRule resourceName<br>
 */
@Getter
@Setter
public class RetrofitSentinelDegradeRuleProperties {

    private Map<String, InstanceProperties> instances = new HashMap<>();
    private Map<String, ConfigProperties> configs = new HashMap<>();

    public void checkAndMerge() {
        instances.forEach((resourceName, bean) -> {
            if (bean.getBaseConfig() != null) {
                ConfigProperties configProperties = configs.get(bean.getBaseConfig());
                if (configProperties == null) {
                    throw new IllegalArgumentException("sentinel degrade instances '" + resourceName + "' : baseConfig " + bean.getBaseConfig() + " not found");
                } else {
                    merge(bean, configProperties);
                }
            }
        });
    }

    public void merge(InstanceProperties instanceProperties, ConfigProperties configProperties) {
        if (instanceProperties.getCount() == DEFAULT) {
            instanceProperties.setCount(configProperties.getCount());
        }
        if (instanceProperties.getGrade() == DEFAULT) {
            instanceProperties.setGrade(configProperties.getGrade());
        }
        if (instanceProperties.getLimitApp() == null) {
            instanceProperties.setLimitApp(configProperties.getLimitApp());
        }
        if (instanceProperties.getMinRequestAmount() == DEFAULT) {
            instanceProperties.setMinRequestAmount(configProperties.getMinRequestAmount());
        }
        if (instanceProperties.getSlowRatioThreshold() == DEFAULT) {
            instanceProperties.setSlowRatioThreshold(configProperties.getSlowRatioThreshold());
        }
        if (instanceProperties.getStatIntervalMs() == DEFAULT) {
            instanceProperties.setStatIntervalMs(configProperties.getStatIntervalMs());
        }
    }

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

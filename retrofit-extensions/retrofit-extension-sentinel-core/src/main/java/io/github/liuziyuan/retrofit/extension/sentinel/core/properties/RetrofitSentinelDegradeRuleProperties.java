package io.github.liuziyuan.retrofit.extension.sentinel.core.properties;

import io.github.liuziyuan.retrofit.extension.sentinel.core.resource.CustomizeDegradeRuleBean;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class RetrofitSentinelDegradeRuleProperties {

    private static final String DEFAULT = "default";

    private Map<String, InstanceProperties> instances = new HashMap<>();
    private Map<String, ConfigProperties> configs = new HashMap<>();

//    public Optional<InstanceProperties> findDegradeRuleProperties(String name) {
//        InstanceProperties instanceProperties = instances.get(name);
//        if (instanceProperties == null) {
//            instanceProperties = configs.get(DEFAULT);
//        } else if (configs.get(DEFAULT) != null) {
//            ConfigUtil.mergePropertiesIfAny(instanceProperties, configs.get(DEFAULT));
//        }
//        return Optional.ofNullable(instanceProperties);
//    }


    @Getter
    @Setter
    public static class InstanceProperties extends CustomizeDegradeRuleBean {
        private String baseConfig;
    }

    @Getter
    @Setter
    public static class ConfigProperties extends CustomizeDegradeRuleBean {
        private Class<?>[] interfaceClazz;
    }

}

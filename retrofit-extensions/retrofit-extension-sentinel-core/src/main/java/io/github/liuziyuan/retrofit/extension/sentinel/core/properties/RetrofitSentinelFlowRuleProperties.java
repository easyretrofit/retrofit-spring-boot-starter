package io.github.liuziyuan.retrofit.extension.sentinel.core.properties;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import io.github.liuziyuan.retrofit.extension.sentinel.core.resource.CustomizeFlowRuleBean;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static io.github.liuziyuan.retrofit.extension.sentinel.core.AppConstants.DEFAULT;

/**
 * RetrofitSentinelFlowRuleProperties configs have the same function as RetrofitSentinelFlowRule declared on a class <br>
 * The configs key need same name as RetrofitSentinelFlowRule resourceName<br>
 */
@Getter
@Setter
public class RetrofitSentinelFlowRuleProperties {

    private Map<String, InstanceProperties> instances = new HashMap<>();
    private Map<String, ConfigProperties> configs = new HashMap<>();


    public void checkAndMerge() {
        instances.forEach((resourceName, bean) -> {
            if (bean.getBaseConfig() != null) {
                ConfigProperties configProperties = configs.get(bean.getBaseConfig());
                if (configProperties == null) {
                    throw new IllegalArgumentException("sentinel flow instances '" + resourceName + "' : baseConfig " + bean.getBaseConfig() + " not found");
                } else {
                    merge(bean, configProperties);
                }
            }
        });
    }

    private void merge(InstanceProperties instanceProperties, ConfigProperties configProperties) {
        if (instanceProperties.getCount() == DEFAULT) {
            instanceProperties.setCount(configProperties.getCount());
        }
        if (instanceProperties.getGrade() == DEFAULT) {
            instanceProperties.setGrade(configProperties.getGrade());
        }
        if (instanceProperties.getStrategy() == DEFAULT) {
            instanceProperties.setStrategy(configProperties.getStrategy());
        }
        if (instanceProperties.getControlBehavior() == DEFAULT) {
            instanceProperties.setControlBehavior(configProperties.getControlBehavior());
        }
        if (instanceProperties.getWarmUpPeriodSec() == DEFAULT) {
            instanceProperties.setWarmUpPeriodSec(configProperties.getWarmUpPeriodSec());
        }
        if (instanceProperties.getMaxQueueingTimeMs() == DEFAULT) {
            instanceProperties.setMaxQueueingTimeMs(configProperties.getMaxQueueingTimeMs());
        }
        if (instanceProperties.getLimitApp() == null) {
            instanceProperties.setLimitApp(configProperties.getLimitApp());
        }
    }

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

package io.github.liuziyuan.retrofit.extension.sentinel.core;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import io.github.liuziyuan.retrofit.core.CDIBeanManager;
import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.core.resource.RetrofitApiServiceBean;
import io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean;
import io.github.liuziyuan.retrofit.extension.sentinel.core.annotation.*;
import io.github.liuziyuan.retrofit.extension.sentinel.core.properties.RetrofitSentinelDegradeRuleProperties;
import io.github.liuziyuan.retrofit.extension.sentinel.core.properties.RetrofitSentinelFlowRuleProperties;
import io.github.liuziyuan.retrofit.extension.sentinel.core.resource.*;
import io.github.liuziyuan.retrofit.extension.sentinel.core.util.ResourceNameUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


/**
 * 从配置文件(properties/yml)中获取Sentinel规则，与@RetrofitSentinelResource声明的RetrofitSentinelDegradeRule和RetrofitSentinelFlowRule注解进行合并 <br>
 * 规则如下：<br>
 * 1. 配置文件中的规则优先级高于注解
 * 2. 配置文件的instances key与 注解的resourceName必须相同
 */
@Slf4j
public class RetrofitSentinelResourceProcessor {

    private long index = 1;
    @Getter
    private Set<FlowRule> flowRules = new HashSet<>();
    @Getter
    private Set<DegradeRule> degradeRules = new HashSet<>();
    @Getter
    private RetrofitSentinelResourceContext sentinelResourceContext;

    private final CDIBeanManager cdiBeanManager;

    public RetrofitSentinelResourceProcessor(RetrofitResourceContext retrofitResourceContext,
                                             CDIBeanManager cdiBeanManager,
                                             RetrofitSentinelDegradeRuleProperties degradeRuleProperties,
                                             RetrofitSentinelFlowRuleProperties flowRuleProperties) {
        this.cdiBeanManager = cdiBeanManager;
        this.sentinelResourceContext = new RetrofitSentinelResourceContext();
        degradeRuleProperties.checkAndMerge();
        flowRuleProperties.checkAndMerge();
        List<RetrofitClientBean> retrofitClients = retrofitResourceContext.getRetrofitClients();
        setFlowRules(retrofitClients, flowRuleProperties);
        setDegradeRules(retrofitClients, degradeRuleProperties);
    }

    private void checkProperties(RetrofitSentinelFlowRuleProperties flowRuleProperties, RetrofitSentinelDegradeRuleProperties degradeRuleProperties) {
//        if (flowRuleProperties.getInstances().size() != flowRuleProperties.getConfigs().size()) {
//            throw new RuntimeException("flow rule properties instances and configs size not equal");
//        }
//        if (degradeRuleProperties.getInstances().size() != degradeRuleProperties.getConfigs().size()) {
//            throw new RuntimeException("degrade rule properties instances and configs size not equal");
//        }
    }

    private void setDegradeRules(List<RetrofitClientBean> retrofitClients, RetrofitSentinelDegradeRuleProperties degradeRuleProperties) {
        for (RetrofitClientBean retrofitClient : retrofitClients) {
            for (RetrofitApiServiceBean retrofitApiServiceBean : retrofitClient.getRetrofitApiServiceBeans()) {
                Class<?> apiClazz = retrofitApiServiceBean.getSelfClazz();
                Class<?> clientClazz = retrofitApiServiceBean.getParentClazz();
                for (Method declaredMethod : apiClazz.getDeclaredMethods()) {
                    Set<DegradeRuleBean> methodAllDegradeRules = new HashSet<>();
                    Annotation[] annotations = declaredMethod.getAnnotations();
                    Set<DegradeRuleBean> apiClazzDegradeRules = getDegradeRuleBeans(apiClazz.getDeclaredAnnotations(), declaredMethod, false, degradeRuleProperties);
                    Set<DegradeRuleBean> clientClazzDegradeRules = getDegradeRuleBeans(clientClazz.getDeclaredAnnotations(), declaredMethod, false, degradeRuleProperties);
                    Set<DegradeRuleBean> methodDegradeRules = getDegradeRuleBeans(annotations, declaredMethod, true, degradeRuleProperties);
                    methodAllDegradeRules.addAll(apiClazzDegradeRules);
                    methodAllDegradeRules.addAll(clientClazzDegradeRules);
                    methodAllDegradeRules.addAll(methodDegradeRules);
                    setToDegradeRules(methodAllDegradeRules);
                }
            }
        }
    }

    private void setToDegradeRules(Set<DegradeRuleBean> degradeRuleBeans) {
        for (DegradeRuleBean degradeRuleBean : degradeRuleBeans) {
            sentinelResourceContext.addFallBackBean(degradeRuleBean.getDefaultResourceName(),
                    new FallBackBean(index, degradeRuleBean.getResourceName(), degradeRuleBean.getFallBackMethodName(), degradeRuleBean));
            DegradeRule degradeRule = new DegradeRule();
            degradeRule.setResource(degradeRuleBean.getDefaultResourceName());
            if (degradeRuleBean.getCount() != AppConstants.DEFAULT) {
                degradeRule.setCount(degradeRuleBean.getCount());
            }
            if (degradeRuleBean.getGrade() != AppConstants.DEFAULT) {
                degradeRule.setGrade(degradeRuleBean.getGrade());
            }
            if (degradeRuleBean.getTimeWindow() != AppConstants.DEFAULT) {
                degradeRule.setTimeWindow(degradeRuleBean.getTimeWindow());
            }
            if (degradeRuleBean.getStatIntervalMs() != AppConstants.DEFAULT) {
                degradeRule.setStatIntervalMs(degradeRuleBean.getStatIntervalMs());
            }
            if (degradeRuleBean.getMinRequestAmount() != AppConstants.DEFAULT) {
                degradeRule.setMinRequestAmount(degradeRuleBean.getMinRequestAmount());
            }
            if (degradeRuleBean.getSlowRatioThreshold() != AppConstants.DEFAULT) {
                degradeRule.setSlowRatioThreshold(degradeRuleBean.getSlowRatioThreshold());
            }
            if (degradeRuleBean.getLimitApp() != null) {
                degradeRule.setLimitApp(degradeRuleBean.getLimitApp());
            }
            degradeRule.setId(index);
            degradeRules.add(degradeRule);
            index++;
        }
    }

    private void setFlowRules(List<RetrofitClientBean> retrofitClients, RetrofitSentinelFlowRuleProperties flowRuleProperties) {
        for (RetrofitClientBean retrofitClient : retrofitClients) {
            for (RetrofitApiServiceBean retrofitApiServiceBean : retrofitClient.getRetrofitApiServiceBeans()) {
                Class<?> apiClazz = retrofitApiServiceBean.getSelfClazz();
                Class<?> clientClazz = retrofitApiServiceBean.getParentClazz();
                for (Method declaredMethod : apiClazz.getDeclaredMethods()) {
                    Set<FlowRuleBean> methodAllFlowRules = new HashSet<>();
                    Annotation[] annotations = declaredMethod.getAnnotations();
                    Set<FlowRuleBean> apiClazzFlowRules = getFlowRuleBeans(apiClazz.getDeclaredAnnotations(), declaredMethod, false, flowRuleProperties);
                    Set<FlowRuleBean> clientClazzFlowRules = getFlowRuleBeans(clientClazz.getDeclaredAnnotations(), declaredMethod, false, flowRuleProperties);
                    Set<FlowRuleBean> methodFlowRules = getFlowRuleBeans(annotations, declaredMethod, true, flowRuleProperties);
                    methodAllFlowRules.addAll(apiClazzFlowRules);
                    methodAllFlowRules.addAll(clientClazzFlowRules);
                    methodAllFlowRules.addAll(methodFlowRules);
                    setToFlowRules(methodAllFlowRules);
                }
            }
        }
    }

    private void setToFlowRules(Set<FlowRuleBean> flowRuleBeans) {
        for (FlowRuleBean flowRuleBean : flowRuleBeans) {
            sentinelResourceContext.addFallBackBean(flowRuleBean.getDefaultResourceName(),
                    new FallBackBean(index, flowRuleBean.getResourceName(), flowRuleBean.getFallBackMethodName(), flowRuleBean));
            FlowRule flowRule = new FlowRule();
            flowRule.setResource(flowRuleBean.getDefaultResourceName());
            if (flowRuleBean.getStrategy() != AppConstants.DEFAULT) {
                flowRule.setStrategy(flowRuleBean.getStrategy());
            }
            if (flowRuleBean.getControlBehavior() != AppConstants.DEFAULT) {
                flowRule.setControlBehavior(flowRuleBean.getControlBehavior());
            }
            if (flowRuleBean.getCount() != AppConstants.DEFAULT) {
                flowRule.setCount(flowRuleBean.getCount());
            }
            if (flowRuleBean.getGrade() != AppConstants.DEFAULT) {
                flowRule.setGrade(flowRuleBean.getGrade());
            }
            if (flowRuleBean.getLimitApp() != null) {
                flowRule.setLimitApp(flowRuleBean.getLimitApp());
            }
            if (flowRuleBean.getMaxQueueingTimeMs() != AppConstants.DEFAULT) {
                flowRule.setMaxQueueingTimeMs(flowRuleBean.getMaxQueueingTimeMs());
            }
            if (flowRuleBean.getWarmUpPeriodSec() != AppConstants.DEFAULT) {
                flowRule.setWarmUpPeriodSec(flowRuleBean.getWarmUpPeriodSec());
            }
            flowRule.setId(index);
            flowRules.add(flowRule);
            index++;
        }
    }

    private Set<FlowRuleBean> getFlowRuleBeans(Annotation[] annotations, Method declaredMethod, boolean isMethod, RetrofitSentinelFlowRuleProperties flowRuleProperties) {
        Set<FlowRuleBean> flowRuleList = new HashSet<>();
        for (Annotation annotation : annotations) {
            if (annotation instanceof RetrofitSentinelFlowRules) {
                RetrofitSentinelFlowRule[] floatRuleValues = ((RetrofitSentinelFlowRules) annotation).value();
                Arrays.stream(floatRuleValues).forEach(value -> {
                    CustomizeFlowRuleBean properties = getCustomizeFlowRuleBean(value, isMethod, flowRuleProperties);
                    FlowRuleBean flowRuleBean = getFlowRuleBean(value, properties);
                    if (flowRuleBean != null) {
                        flowRuleBean.setDefaultResourceName(ResourceNameUtil.getConventionResourceName(declaredMethod));
                        flowRuleList.add(flowRuleBean);
                    }
                });
            } else if (annotation instanceof RetrofitSentinelFlowRule) {
                CustomizeFlowRuleBean properties = getCustomizeFlowRuleBean((RetrofitSentinelFlowRule) annotation, isMethod, flowRuleProperties);
                FlowRuleBean flowRuleBean = getFlowRuleBean((RetrofitSentinelFlowRule) annotation, properties);
                if (flowRuleBean != null) {
                    flowRuleBean.setDefaultResourceName(ResourceNameUtil.getConventionResourceName(declaredMethod));
                    flowRuleList.add(flowRuleBean);
                }
            }
        }
        return flowRuleList;
    }

    private Set<DegradeRuleBean> getDegradeRuleBeans(Annotation[] annotations, Method declaredMethod, boolean isMethod, RetrofitSentinelDegradeRuleProperties degradeRuleProperties) {
        Set<DegradeRuleBean> degradeRuleList = new HashSet<>();
        for (Annotation annotation : annotations) {
            if (annotation instanceof RetrofitSentinelDegrades) {
                RetrofitSentinelDegradeRule[] floatRuleValues = ((RetrofitSentinelDegrades) annotation).value();
                Arrays.stream(floatRuleValues).forEach(value -> {
                    CustomizeDegradeRuleBean properties = getCustomizeDegradeRuleBean(value, isMethod, degradeRuleProperties);
                    DegradeRuleBean degradeRuleBean = getDegradeRuleBean(value, properties);
                    if (degradeRuleBean != null) {
                        degradeRuleBean.setDefaultResourceName(ResourceNameUtil.getConventionResourceName(declaredMethod));
                        degradeRuleList.add(degradeRuleBean);
                    }
                });
            } else if (annotation instanceof RetrofitSentinelDegradeRule) {
                CustomizeDegradeRuleBean properties = getCustomizeDegradeRuleBean((RetrofitSentinelDegradeRule) annotation, isMethod, degradeRuleProperties);
                DegradeRuleBean degradeRuleBean = getDegradeRuleBean((RetrofitSentinelDegradeRule) annotation, properties);
                if (degradeRuleBean != null) {
                    degradeRuleBean.setDefaultResourceName(ResourceNameUtil.getConventionResourceName(declaredMethod));
                    degradeRuleList.add(degradeRuleBean);
                }
            }
        }
        return degradeRuleList;
    }

    @SneakyThrows
    private CustomizeDegradeRuleBean getCustomizeDegradeRuleBean(RetrofitSentinelDegradeRule annotation, boolean isMethod, RetrofitSentinelDegradeRuleProperties degradeRuleProperties) {
        CustomizeDegradeRuleBean customizeDegradeRuleBean = new CustomizeDegradeRuleBean();
        if (!isMethod) {
            Map<String, RetrofitSentinelDegradeRuleProperties.ConfigProperties> configs = degradeRuleProperties.getConfigs();
            RetrofitSentinelDegradeRuleProperties.ConfigProperties configProperties = configs.get(annotation.resourceName());
            if (configProperties == null) {
                return null;
            }
            BeanUtils.copyProperties(customizeDegradeRuleBean, configProperties);
        } else {
            Map<String, RetrofitSentinelDegradeRuleProperties.InstanceProperties> instances = degradeRuleProperties.getInstances();
            RetrofitSentinelDegradeRuleProperties.InstanceProperties instanceProperties = instances.get(annotation.resourceName());
            if (instanceProperties == null) {
                return null;
            }
            BeanUtils.copyProperties(customizeDegradeRuleBean, instanceProperties);
        }
        return customizeDegradeRuleBean;
    }

    @SneakyThrows
    private CustomizeFlowRuleBean getCustomizeFlowRuleBean(RetrofitSentinelFlowRule annotation, boolean isMethod, RetrofitSentinelFlowRuleProperties flowRuleProperties) {
        CustomizeFlowRuleBean customizeFlowRuleBean = new CustomizeFlowRuleBean();
        if (!isMethod) {
            Map<String, RetrofitSentinelFlowRuleProperties.ConfigProperties> configs = flowRuleProperties.getConfigs();
            RetrofitSentinelFlowRuleProperties.ConfigProperties configProperties = configs.get(annotation.resourceName());
            if (configProperties == null) {
                return null;
            }
            BeanUtils.copyProperties(customizeFlowRuleBean, configProperties);
        } else {
            Map<String, RetrofitSentinelFlowRuleProperties.InstanceProperties> instances = flowRuleProperties.getInstances();
            RetrofitSentinelFlowRuleProperties.InstanceProperties instanceProperties = instances.get(annotation.resourceName());
            if (instanceProperties == null) {
                return null;
            }
            BeanUtils.copyProperties(customizeFlowRuleBean, instanceProperties);
        }
        return customizeFlowRuleBean;
    }

    @SneakyThrows
    private FlowRuleBean getFlowRuleBean(RetrofitSentinelFlowRule annotation, CustomizeFlowRuleBean properties) {
        Class<? extends BaseFlowRuleConfig> configClazz = annotation.config();
        FlowRuleBean flowRuleBean = new FlowRuleBean();
        if (properties == null) {
            if (configClazz == BaseFlowRuleConfig.class) {
                return null;
            }
            BaseFlowRuleConfig bean = cdiBeanManager.getBean(configClazz);
            CustomizeFlowRuleBean customizeFlowRuleBean;
            if (bean != null) {
                customizeFlowRuleBean = bean.build();
                try {
                    BeanUtils.copyProperties(flowRuleBean, customizeFlowRuleBean);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    bean = configClazz.newInstance();
                    customizeFlowRuleBean = bean.build();
                    BeanUtils.copyProperties(flowRuleBean, customizeFlowRuleBean);
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            BeanUtils.copyProperties(flowRuleBean, properties);
        }
        flowRuleBean.setResourceName(annotation.resourceName());
        flowRuleBean.setFallBackMethodName(annotation.fallbackMethod());
        flowRuleBean.setConfigClazz(configClazz);
        return flowRuleBean;
    }

    @SneakyThrows
    private DegradeRuleBean getDegradeRuleBean(RetrofitSentinelDegradeRule annotation, CustomizeDegradeRuleBean properties) {
        Class<? extends BaseDegradeRuleConfig> configClazz = annotation.config();
        DegradeRuleBean degradeRuleBean = new DegradeRuleBean();
        if (properties == null) {
            if (configClazz == BaseDegradeRuleConfig.class) {
                return null;
            }
            BaseDegradeRuleConfig bean = cdiBeanManager.getBean(configClazz);

            CustomizeDegradeRuleBean customizeDegradeRuleBean;
            if (bean != null) {
                customizeDegradeRuleBean = bean.build();
                try {
                    BeanUtils.copyProperties(degradeRuleBean, customizeDegradeRuleBean);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
                try {
                    bean = configClazz.newInstance();
                    customizeDegradeRuleBean = bean.build();
                    BeanUtils.copyProperties(degradeRuleBean, customizeDegradeRuleBean);
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            BeanUtils.copyProperties(degradeRuleBean, properties);
        }

        degradeRuleBean.setResourceName(annotation.resourceName());
        degradeRuleBean.setFallBackMethodName(annotation.fallbackMethod());
        degradeRuleBean.setConfigClazz(configClazz);
        return degradeRuleBean;
    }


}

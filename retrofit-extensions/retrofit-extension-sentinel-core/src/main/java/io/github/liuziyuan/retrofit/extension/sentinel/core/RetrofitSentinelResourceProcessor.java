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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


@Slf4j
public class RetrofitSentinelResourceProcessor {
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
        List<RetrofitClientBean> retrofitClients = retrofitResourceContext.getRetrofitClients();
        setFlowRules(retrofitClients, flowRuleProperties);
        setDegradeRules(retrofitClients, degradeRuleProperties);
    }

    private void setFlowRules(List<RetrofitClientBean> retrofitClients, RetrofitSentinelFlowRuleProperties flowRuleProperties) {
        for (RetrofitClientBean retrofitClient : retrofitClients) {
            for (RetrofitApiServiceBean retrofitApiServiceBean : retrofitClient.getRetrofitApiServiceBeans()) {
                Class<?> apiClazz = retrofitApiServiceBean.getSelfClazz();
                Class<?> clientClazz = retrofitApiServiceBean.getParentClazz();
                for (Method declaredMethod : apiClazz.getDeclaredMethods()) {
                    Set<FlowRuleBean> methodAllFlowRules = new HashSet<>();
                    Annotation[] annotations = declaredMethod.getAnnotations();
                    Set<FlowRuleBean> apiClazzFlowRules = getFlowRuleBeansByAnnotation(apiClazz.getDeclaredAnnotations(), declaredMethod);
                    Set<FlowRuleBean> clientClazzFlowRules = getFlowRuleBeansByAnnotation(clientClazz.getDeclaredAnnotations(), declaredMethod);
                    Set<FlowRuleBean> methodFlowRules = getFlowRuleBeansByAnnotation(annotations, declaredMethod);
                    methodAllFlowRules.addAll(apiClazzFlowRules);
                    methodAllFlowRules.addAll(clientClazzFlowRules);
                    methodAllFlowRules.addAll(methodFlowRules);
                    setToFlowRules(flowRules, declaredMethod, methodAllFlowRules);
                }
            }
        }
//        FlowRuleManager.loadRules(new ArrayList<>(flowRules));
    }

    private void setDegradeRules(List<RetrofitClientBean> retrofitClients, RetrofitSentinelDegradeRuleProperties degradeRuleProperties) {
        for (RetrofitClientBean retrofitClient : retrofitClients) {
            for (RetrofitApiServiceBean retrofitApiServiceBean : retrofitClient.getRetrofitApiServiceBeans()) {
                Class<?> apiClazz = retrofitApiServiceBean.getSelfClazz();
                Class<?> clientClazz = retrofitApiServiceBean.getParentClazz();
                for (Method declaredMethod : apiClazz.getDeclaredMethods()) {
                    Set<DegradeRuleBean> methodAllDegradeRules = new HashSet<>();
                    Annotation[] annotations = declaredMethod.getAnnotations();
                    Set<DegradeRuleBean> apiClazzDegradeRules = getDegradeRuleBeansByAnnotation(apiClazz.getDeclaredAnnotations(), declaredMethod);
                    Set<DegradeRuleBean> clientClazzDegradeRules = getDegradeRuleBeansByAnnotation(clientClazz.getDeclaredAnnotations(), declaredMethod);
                    Set<DegradeRuleBean> methodDegradeRules = getDegradeRuleBeansByAnnotation(annotations, declaredMethod);
                    methodAllDegradeRules.addAll(apiClazzDegradeRules);
                    methodAllDegradeRules.addAll(clientClazzDegradeRules);
                    methodAllDegradeRules.addAll(methodDegradeRules);
                    setToDegradeRules(degradeRules, declaredMethod, methodAllDegradeRules);
                }
            }
        }
//        DegradeRuleManager.loadRules(new ArrayList<>(degradeRules));
    }

    private void setToFlowRules(Set<FlowRule> flowRules, Method method, Set<FlowRuleBean> flowRuleBeans) {
        for (FlowRuleBean flowRuleBean : flowRuleBeans) {
            sentinelResourceContext.addFallBackBean(flowRuleBean.getDefaultResourceName(),
                    new FallBackBean(flowRuleBean.getResourceName(), flowRuleBean.getFallBackMethodName(), flowRuleBean.getConfigClazz()));
            FlowRule flowRule = new FlowRule();
            flowRule.setResource(flowRuleBean.getDefaultResourceName());
            flowRule.setCount(flowRuleBean.getCount());
            flowRule.setWarmUpPeriodSec(flowRuleBean.getWarmUpPeriodSec());
            flowRule.setMaxQueueingTimeMs(flowRuleBean.getMaxQueueingTimeMs());
            flowRule.setGrade(flowRuleBean.getGrade());
            flowRule.setStrategy(flowRuleBean.getStrategy());
            flowRule.setControlBehavior(flowRuleBean.getControlBehavior());
            flowRule.setLimitApp(flowRuleBean.getLimitApp());
            flowRules.add(flowRule);
        }
    }

    private void setToDegradeRules(Set<DegradeRule> degradeRules, Method method, Set<DegradeRuleBean> degradeRuleBeans) {
        for (DegradeRuleBean degradeRuleBean : degradeRuleBeans) {
            sentinelResourceContext.addFallBackBean(degradeRuleBean.getDefaultResourceName(),
                    new FallBackBean(degradeRuleBean.getResourceName(), degradeRuleBean.getFallBackMethodName(), degradeRuleBean.getConfigClazz()));
            DegradeRule degradeRule = new DegradeRule();
            degradeRule.setResource(degradeRuleBean.getDefaultResourceName());
            degradeRule.setCount(degradeRuleBean.getCount());
            degradeRule.setGrade(degradeRuleBean.getGrade());
            degradeRule.setSlowRatioThreshold(degradeRuleBean.getSlowRatioThreshold());
            degradeRule.setStatIntervalMs(degradeRuleBean.getStatIntervalMs());
            degradeRule.setTimeWindow(degradeRuleBean.getTimeWindow());
            degradeRule.setMinRequestAmount(degradeRuleBean.getMinRequestAmount());
            degradeRule.setLimitApp(degradeRuleBean.getLimitApp());
            degradeRules.add(degradeRule);
        }
    }

    private Set<FlowRuleBean> getFlowRuleBeansByAnnotation(Annotation[] annotations, Method declaredMethod) {
        Set<FlowRuleBean> flowRuleList = new HashSet<>();
        for (Annotation annotation : annotations) {
            if (annotation instanceof RetrofitSentinelFlowRules) {
                RetrofitSentinelFlowRule[] floatRuleValues = ((RetrofitSentinelFlowRules) annotation).value();
                Arrays.stream(floatRuleValues).forEach(value -> {
                    FlowRuleBean flowRuleBean = getFlowRuleBean(value);
                    if (flowRuleBean != null) {
                        flowRuleBean.setDefaultResourceName(ResourceNameUtil.getConventionResourceName(declaredMethod));
                        flowRuleList.add(flowRuleBean);
                    }
                });
            } else if (annotation instanceof RetrofitSentinelFlowRule) {
                FlowRuleBean flowRuleBean = getFlowRuleBean((RetrofitSentinelFlowRule) annotation);
                if (flowRuleBean != null) {
                    flowRuleBean.setDefaultResourceName(ResourceNameUtil.getConventionResourceName(declaredMethod));
                    flowRuleList.add(flowRuleBean);
                }
            }
        }
        return flowRuleList;
    }


    private Set<DegradeRuleBean> getDegradeRuleBeansByAnnotation(Annotation[] annotations, Method declaredMethod) {
        Set<DegradeRuleBean> degradeRuleList = new HashSet<>();
        for (Annotation annotation : annotations) {
            if (annotation instanceof RetrofitSentinelDegrades) {
                RetrofitSentinelDegradeRule[] floatRuleValues = ((RetrofitSentinelDegrades) annotation).value();
                Arrays.stream(floatRuleValues).forEach(value -> {
                    DegradeRuleBean degradeRuleBean = getDegradeRuleBean(value);
                    if (degradeRuleBean != null) {
                        degradeRuleBean.setDefaultResourceName(ResourceNameUtil.getConventionResourceName(declaredMethod));
                        degradeRuleList.add(degradeRuleBean);
                    }
                });
            } else if (annotation instanceof RetrofitSentinelDegradeRule) {
                DegradeRuleBean degradeRuleBean = getDegradeRuleBean((RetrofitSentinelDegradeRule) annotation);
                if (degradeRuleBean != null) {
                    degradeRuleBean.setDefaultResourceName(ResourceNameUtil.getConventionResourceName(declaredMethod));
                    degradeRuleList.add(degradeRuleBean);
                }
            }
        }
        return degradeRuleList;
    }

    private FlowRuleBean getFlowRuleBean(RetrofitSentinelFlowRule annotation) {
        Class<? extends BaseFlowRuleConfig> configClazz = annotation.config();
        if (configClazz == BaseFlowRuleConfig.class) {
            return null;
        }
        BaseFlowRuleConfig bean = cdiBeanManager.getBean(configClazz);
        FlowRuleBean flowRuleBean = new FlowRuleBean();
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
        flowRuleBean.setResourceName(annotation.resourceName());
        flowRuleBean.setFallBackMethodName(annotation.fallbackMethod());
        flowRuleBean.setConfigClazz(configClazz);
        return flowRuleBean;
    }

    private DegradeRuleBean getDegradeRuleBean(RetrofitSentinelDegradeRule annotation) {
        Class<? extends BaseDegradeRuleConfig> configClazz = annotation.config();
        if (configClazz == BaseDegradeRuleConfig.class) {
            return null;
        }
        BaseDegradeRuleConfig bean = cdiBeanManager.getBean(configClazz);
        DegradeRuleBean degradeRuleBean = new DegradeRuleBean();
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
        degradeRuleBean.setResourceName(annotation.resourceName());
        degradeRuleBean.setFallBackMethodName(annotation.fallbackMethod());
        degradeRuleBean.setConfigClazz(configClazz);
        return degradeRuleBean;
    }


}

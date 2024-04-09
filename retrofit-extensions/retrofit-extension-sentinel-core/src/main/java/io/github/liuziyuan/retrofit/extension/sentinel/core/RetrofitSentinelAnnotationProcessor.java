package io.github.liuziyuan.retrofit.extension.sentinel.core;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import io.github.liuziyuan.retrofit.core.CDIBeanManager;
import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.core.resource.RetrofitApiServiceBean;
import io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean;
import io.github.liuziyuan.retrofit.extension.sentinel.core.annotation.*;
import io.github.liuziyuan.retrofit.extension.sentinel.core.resource.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;


@Slf4j
public class RetrofitSentinelAnnotationProcessor {
    @Getter
    Set<FlowRule> flowRules = new HashSet<>();

    @Getter
    Set<DegradeRule> degradeRules = new HashSet<>();
    private final RetrofitResourceContext retrofitResourceContext;
    private final CDIBeanManager cdiBeanManager;


    public RetrofitSentinelAnnotationProcessor(RetrofitResourceContext retrofitResourceContext, CDIBeanManager cdiBeanManager) {
        this.retrofitResourceContext = retrofitResourceContext;
        this.cdiBeanManager = cdiBeanManager;
    }

    public void process() {
        List<RetrofitClientBean> retrofitClients = retrofitResourceContext.getRetrofitClients();
        registFlowRules(retrofitClients);
        registDegradeRules(retrofitClients);
    }

    private void registFlowRules(List<RetrofitClientBean> retrofitClients) {
        for (RetrofitClientBean retrofitClient : retrofitClients) {
            for (RetrofitApiServiceBean retrofitApiServiceBean : retrofitClient.getRetrofitApiServiceBeans()) {
                Class<?> apiClazz = retrofitApiServiceBean.getSelfClazz();
                Class<?> clientClazz = retrofitApiServiceBean.getParentClazz();
                Set<FlowRuleBean> apiClazzFlowRules = getFlowRuleBeansByAnnotation(apiClazz.getDeclaredAnnotations(), false);
                Set<FlowRuleBean> clientClazzFlowRules = getFlowRuleBeansByAnnotation(clientClazz.getDeclaredAnnotations(), false);
                for (Method declaredMethod : apiClazz.getDeclaredMethods()) {
                    Set<FlowRuleBean> methodAllFlowRules = new HashSet<>();
                    Annotation[] annotations = declaredMethod.getAnnotations();
                    Set<FlowRuleBean> methodFlowRules = getFlowRuleBeansByAnnotation(annotations, true);
                    methodAllFlowRules.addAll(apiClazzFlowRules);
                    methodAllFlowRules.addAll(clientClazzFlowRules);
                    methodAllFlowRules.addAll(methodFlowRules);
                    setToFlowRules(apiClazz.getName(), declaredMethod.getName(), methodAllFlowRules);
                }
            }
        }
//        FlowRuleManager.loadRules(flowRules);
    }

    private void registDegradeRules(List<RetrofitClientBean> retrofitClients) {
        for (RetrofitClientBean retrofitClient : retrofitClients) {
            for (RetrofitApiServiceBean retrofitApiServiceBean : retrofitClient.getRetrofitApiServiceBeans()) {
                Class<?> apiClazz = retrofitApiServiceBean.getSelfClazz();
                Class<?> clientClazz = retrofitApiServiceBean.getParentClazz();
                Set<DegradeRuleBean> apiClazzDegradeRules = getDegradeRuleBeansByAnnotation(apiClazz.getDeclaredAnnotations(), false);
                Set<DegradeRuleBean> clientClazzDegradeRules = getDegradeRuleBeansByAnnotation(clientClazz.getDeclaredAnnotations(), false);
                for (Method declaredMethod : apiClazz.getDeclaredMethods()) {
                    Set<DegradeRuleBean> methodAllDegradeRules = new HashSet<>();
                    Annotation[] annotations = declaredMethod.getAnnotations();
                    Set<DegradeRuleBean> methodDegradeRules = getDegradeRuleBeansByAnnotation(annotations, true);
                    methodAllDegradeRules.addAll(apiClazzDegradeRules);
                    methodAllDegradeRules.addAll(clientClazzDegradeRules);
                    methodAllDegradeRules.addAll(methodDegradeRules);
                    setToDegradeRules(apiClazz.getName(), declaredMethod.getName(), methodAllDegradeRules);
                }
            }
        }
//        DegradeRuleManager.loadRules(degradeRules);
    }

    private void setToFlowRules(String clazzName, String methodName, Set<FlowRuleBean> flowRuleBeans) {
        for (FlowRuleBean flowRuleBean : flowRuleBeans) {
            String resourceName;
            if (StringUtils.isEmpty(flowRuleBean.getResourceName())) {
                resourceName = clazzName + "." + methodName;
            } else {
                resourceName = flowRuleBean.getResourceName();
            }
            FlowRule flowRule = new FlowRule();
            flowRule.setResource(resourceName);
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

    private void setToDegradeRules(String clazzName, String methodName, Set<DegradeRuleBean> degradeRuleBeans) {


        for (DegradeRuleBean degradeRuleBean : degradeRuleBeans) {
            String resourceName;
            if (StringUtils.isEmpty(degradeRuleBean.getResourceName())) {
                resourceName = clazzName + "." + methodName;
            } else {
                resourceName = degradeRuleBean.getResourceName();
            }
            DegradeRule degradeRule = new DegradeRule();
            degradeRule.setResource(resourceName);
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

    private Set<FlowRuleBean> getFlowRuleBeansByAnnotation(Annotation[] annotations, boolean isMethod) {
        Set<FlowRuleBean> flowRuleList = new HashSet<>();
        for (Annotation annotation : annotations) {
            if (annotation instanceof RetrofitSentinelFlowRules) {
                RetrofitSentinelFlowRule[] floatRuleValues = ((RetrofitSentinelFlowRules) annotation).value();
                Arrays.stream(floatRuleValues).forEach(value -> flowRuleList.add(getFlowRuleBean(value, isMethod)));
            } else if (annotation instanceof RetrofitSentinelFlowRule) {
                FlowRuleBean flowRuleBean = getFlowRuleBean((RetrofitSentinelFlowRule) annotation, isMethod);
                flowRuleList.add(flowRuleBean);
            }
        }
        return flowRuleList;
    }


    private Set<DegradeRuleBean> getDegradeRuleBeansByAnnotation(Annotation[] annotations, boolean isMethod) {
        Set<DegradeRuleBean> degradeRuleList = new HashSet<>();
        for (Annotation annotation : annotations) {
            if (annotation instanceof RetrofitSentinelDegrades) {
                RetrofitSentinelDegradeRule[] floatRuleValues = ((RetrofitSentinelDegrades) annotation).value();
                Arrays.stream(floatRuleValues).forEach(value -> degradeRuleList.add(getDegradeRuleBean(value, isMethod)));
            } else if (annotation instanceof RetrofitSentinelDegradeRule) {
                DegradeRuleBean degradeRuleBean = getDegradeRuleBean((RetrofitSentinelDegradeRule) annotation, isMethod);
                degradeRuleList.add(degradeRuleBean);
            }
        }
        return degradeRuleList;
    }

    private FlowRuleBean getFlowRuleBean(RetrofitSentinelFlowRule annotation, boolean isMethod) {
        Class<? extends BaseFlowRuleConfig> configClazz = annotation.config();
        BaseFlowRuleConfig bean = cdiBeanManager.getBean(configClazz);
        FlowRuleBean flowRuleBean;
        if (bean != null) {
            flowRuleBean = (FlowRuleBean) bean.build();
        } else {
            flowRuleBean = new FlowRuleBean();
        }
        if (isMethod) {
            flowRuleBean.setResourceName(null);
        } else {
            flowRuleBean.setResourceName(annotation.resourceName());
        }
        return flowRuleBean;
    }

    private DegradeRuleBean getDegradeRuleBean(RetrofitSentinelDegradeRule annotation, boolean isMethod) {
        Class<? extends BaseDegradeRuleConfig> configClazz = annotation.config();
        BaseDegradeRuleConfig bean = cdiBeanManager.getBean(configClazz);
        DegradeRuleBean degradeRuleBean;
        if (bean != null) {
            degradeRuleBean = (DegradeRuleBean) bean.build();
        } else {
            degradeRuleBean = new DegradeRuleBean();
        }

        if (isMethod) {
            degradeRuleBean.setResourceName(null);
        } else {
            degradeRuleBean.setResourceName(annotation.resourceName());
        }
        return degradeRuleBean;
    }


}

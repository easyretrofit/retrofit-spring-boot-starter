package io.github.liuziyuan.retrofit.extension.sentinel.core;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.core.resource.RetrofitApiServiceBean;
import io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean;
import io.github.liuziyuan.retrofit.extension.sentinel.core.annotation.*;
import io.github.liuziyuan.retrofit.extension.sentinel.core.resource.RetrofitSentinelDegradeRuleBean;
import io.github.liuziyuan.retrofit.extension.sentinel.core.resource.RetrofitSentinelFlowRuleBean;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
public class RetrofitSentinelAnnotationProcessor {
    List<FlowRule> flowRules = new ArrayList<>();
    List<DegradeRule> degradeRules = new ArrayList<>();
    private final RetrofitResourceContext retrofitResourceContext;


    public RetrofitSentinelAnnotationProcessor(RetrofitResourceContext retrofitResourceContext) {
        this.retrofitResourceContext = retrofitResourceContext;
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
                Set<RetrofitSentinelFlowRuleBean> apiClazzFlowRules = getFlowRules(apiClazz.getDeclaredAnnotations());
                Set<RetrofitSentinelFlowRuleBean> clientClazzFlowRules = getFlowRules(clientClazz.getDeclaredAnnotations());
                for (Method declaredMethod : apiClazz.getDeclaredMethods()) {
                    Set<RetrofitSentinelFlowRuleBean> methodAllFlowRules = new HashSet<>();
                    Annotation[] annotations = declaredMethod.getAnnotations();
                    Set<RetrofitSentinelFlowRuleBean> methodFlowRules = getFlowRules(annotations);
                    methodAllFlowRules.addAll(apiClazzFlowRules);
                    methodAllFlowRules.addAll(clientClazzFlowRules);
                    methodAllFlowRules.addAll(methodFlowRules);
                    setToFlowRules(apiClazz.getName(), declaredMethod.getName(), methodAllFlowRules);
                }
            }
        }
        FlowRuleManager.loadRules(flowRules);
    }

    private void registDegradeRules(List<RetrofitClientBean> retrofitClients) {
        for (RetrofitClientBean retrofitClient : retrofitClients) {
            for (RetrofitApiServiceBean retrofitApiServiceBean : retrofitClient.getRetrofitApiServiceBeans()) {
                Class<?> apiClazz = retrofitApiServiceBean.getSelfClazz();
                Class<?> clientClazz = retrofitApiServiceBean.getParentClazz();
                Set<RetrofitSentinelDegradeRuleBean> apiClazzDegradeRules = getDegradeRules(apiClazz.getDeclaredAnnotations());
                Set<RetrofitSentinelDegradeRuleBean> clientClazzDegradeRules = getDegradeRules(clientClazz.getDeclaredAnnotations());
                for (Method declaredMethod : apiClazz.getDeclaredMethods()) {
                    Set<RetrofitSentinelDegradeRuleBean> methodAllDegradeRules = new HashSet<>();
                    Annotation[] annotations = declaredMethod.getAnnotations();
                    Set<RetrofitSentinelDegradeRuleBean> methodDegradeRules = getDegradeRules(annotations);
                    methodAllDegradeRules.addAll(apiClazzDegradeRules);
                    methodAllDegradeRules.addAll(clientClazzDegradeRules);
                    methodAllDegradeRules.addAll(methodDegradeRules);
                    setToDegradeRules(apiClazz.getName(), declaredMethod.getName(), methodAllDegradeRules);
                }
            }
        }
        DegradeRuleManager.loadRules(degradeRules);
    }

    private void setToFlowRules(String clazzName, String methodName, Set<RetrofitSentinelFlowRuleBean> flowRuleBeans) {
        String resourceName = clazzName + "." + methodName;
        for (RetrofitSentinelFlowRuleBean flowRuleBean : flowRuleBeans) {
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

    private void setToDegradeRules(String clazzName, String methodName, Set<RetrofitSentinelDegradeRuleBean> degradeRuleBeans) {
        String resourceName = clazzName + "." + methodName;
        for (RetrofitSentinelDegradeRuleBean degradeRuleBean : degradeRuleBeans) {
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

    private Set<RetrofitSentinelFlowRuleBean> getFlowRules(Annotation[] annotations) {
        Set<RetrofitSentinelFlowRuleBean> flowRuleList = new HashSet<>();
        for (Annotation annotation : annotations) {
            if (annotation instanceof RetrofitSentinelFlowRules) {
                RetrofitSentinelFlowRule[] floatRuleValues = ((RetrofitSentinelFlowRules) annotation).value();
                Arrays.stream(floatRuleValues).forEach(value -> flowRuleList.add(getRetrofitSentinelFlowRuleBean(value)));
            } else if (annotation instanceof RetrofitSentinelFlowRule) {
                RetrofitSentinelFlowRuleBean flowRuleBean = getRetrofitSentinelFlowRuleBean((RetrofitSentinelFlowRule) annotation);
                flowRuleList.add(flowRuleBean);
            } else if (annotation instanceof RetrofitSentinelRateLimiters) {
                RetrofitSentinelRateLimiterFlowRule[] floatRuleValues = ((RetrofitSentinelRateLimiters) annotation).value();
                Arrays.stream(floatRuleValues).forEach(value -> flowRuleList.add(getRetrofitSentinelReteLimitFlowRuleBean(value)));
            } else if (annotation instanceof RetrofitSentinelRateLimiterFlowRule) {
                RetrofitSentinelFlowRuleBean flowRuleBean = getRetrofitSentinelReteLimitFlowRuleBean((RetrofitSentinelRateLimiterFlowRule) annotation);
                flowRuleList.add(flowRuleBean);
            } else if (annotation instanceof RetrofitSentinelWarmUps) {
                RetrofitSentinelWarmUpFlowRule[] floatRuleValues = ((RetrofitSentinelWarmUps) annotation).value();
                Arrays.stream(floatRuleValues).forEach(value -> flowRuleList.add(getRetrofitSentinelWarmUpFlowRuleBean(value)));
            } else if (annotation instanceof RetrofitSentinelWarmUpFlowRule) {
                RetrofitSentinelFlowRuleBean flowRuleBean = getRetrofitSentinelWarmUpFlowRuleBean((RetrofitSentinelWarmUpFlowRule) annotation);
                flowRuleList.add(flowRuleBean);
            }
        }
        return flowRuleList;
    }


    private Set<RetrofitSentinelDegradeRuleBean> getDegradeRules(Annotation[] annotations) {
        Set<RetrofitSentinelDegradeRuleBean> degradeRuleList = new HashSet<>();
        for (Annotation annotation : annotations) {
            if (annotation instanceof RetrofitSentinelDegrades) {
                RetrofitSentinelDegradeRule[] floatRuleValues = ((RetrofitSentinelDegrades) annotation).value();
                Arrays.stream(floatRuleValues).forEach(value -> degradeRuleList.add(getRetrofitSentinelDegradeRuleBean(value)));
            } else if (annotation instanceof RetrofitSentinelDegradeRule) {
                RetrofitSentinelDegradeRuleBean degradeRuleBean = getRetrofitSentinelDegradeRuleBean((RetrofitSentinelDegradeRule) annotation);
                degradeRuleList.add(degradeRuleBean);
            }
        }
        return degradeRuleList;
    }

    private RetrofitSentinelFlowRuleBean getRetrofitSentinelFlowRuleBean(RetrofitSentinelFlowRule annotation) {
        RetrofitSentinelFlowRuleBean flowRuleBean = new RetrofitSentinelFlowRuleBean();
        flowRuleBean.setCount(annotation.count());
        flowRuleBean.setGrade(annotation.grade());
        flowRuleBean.setMaxQueueingTimeMs(annotation.maxQueueingTimeMs());
        flowRuleBean.setStrategy(annotation.strategy());
        flowRuleBean.setControlBehavior(annotation.controlBehavior());
        flowRuleBean.setWarmUpPeriodSec(annotation.warmUpPeriodSec());
        flowRuleBean.setLimitApp(annotation.limitApp());
        return flowRuleBean;
    }

    private RetrofitSentinelFlowRuleBean getRetrofitSentinelWarmUpFlowRuleBean(RetrofitSentinelWarmUpFlowRule annotation) {
        RetrofitSentinelFlowRuleBean flowRuleBean = new RetrofitSentinelFlowRuleBean();
        flowRuleBean.setCount(annotation.count());
        flowRuleBean.setGrade(annotation.grade());
        flowRuleBean.setWarmUpPeriodSec(annotation.warmUpPeriodSec());
        flowRuleBean.setLimitApp(annotation.limitApp());
        if (annotation.rateLimit() != 0) {
            flowRuleBean.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_WARM_UP_RATE_LIMITER);
            flowRuleBean.setMaxQueueingTimeMs(annotation.rateLimit());
        } else {
            flowRuleBean.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_WARM_UP);
        }

        return flowRuleBean;
    }

    private RetrofitSentinelFlowRuleBean getRetrofitSentinelReteLimitFlowRuleBean(RetrofitSentinelRateLimiterFlowRule annotation) {
        RetrofitSentinelFlowRuleBean flowRuleBean = new RetrofitSentinelFlowRuleBean();
        flowRuleBean.setCount(annotation.count());
        flowRuleBean.setGrade(annotation.grade());
        flowRuleBean.setLimitApp(annotation.limitApp());
        flowRuleBean.setMaxQueueingTimeMs(annotation.rateLimit());
        flowRuleBean.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER);
        return null;
    }

    private RetrofitSentinelDegradeRuleBean getRetrofitSentinelDegradeRuleBean(RetrofitSentinelDegradeRule annotation) {
        RetrofitSentinelDegradeRuleBean degradeRuleBean = new RetrofitSentinelDegradeRuleBean();
        degradeRuleBean.setCount(annotation.count());
        degradeRuleBean.setGrade(annotation.grade());
        degradeRuleBean.setSlowRatioThreshold(annotation.slowRatioThreshold());
        degradeRuleBean.setTimeWindow(annotation.timeWindow());
        degradeRuleBean.setStatIntervalMs(annotation.statIntervalMs());
        degradeRuleBean.setMinRequestAmount(annotation.minRequestAmount());
        degradeRuleBean.setLimitApp(annotation.limitApp());
        return degradeRuleBean;
    }


}

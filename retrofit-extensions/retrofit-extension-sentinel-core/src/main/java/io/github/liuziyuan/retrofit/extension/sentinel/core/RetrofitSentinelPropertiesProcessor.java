package io.github.liuziyuan.retrofit.extension.sentinel.core;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import io.github.liuziyuan.retrofit.core.CDIBeanManager;
import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.core.resource.RetrofitApiServiceBean;
import io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean;
import io.github.liuziyuan.retrofit.extension.sentinel.core.annotation.RetrofitSentinelDegradeRule;
import io.github.liuziyuan.retrofit.extension.sentinel.core.annotation.RetrofitSentinelDegrades;
import io.github.liuziyuan.retrofit.extension.sentinel.core.properties.RetrofitSentinelDegradeRuleProperties;
import io.github.liuziyuan.retrofit.extension.sentinel.core.properties.RetrofitSentinelFlowRuleProperties;
import io.github.liuziyuan.retrofit.extension.sentinel.core.resource.DegradeRuleBean;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class RetrofitSentinelPropertiesProcessor {
    @Getter
    Set<FlowRule> flowRules = new HashSet<>();

    @Getter
    Set<DegradeRule> degradeRules = new HashSet<>();
    private final RetrofitResourceContext retrofitResourceContext;
    private final CDIBeanManager cdiBeanManager;

    public RetrofitSentinelPropertiesProcessor(RetrofitResourceContext retrofitResourceContext,
                                               CDIBeanManager cdiBeanManager,
                                               RetrofitSentinelDegradeRuleProperties degradeRuleProperties,
                                               RetrofitSentinelFlowRuleProperties flowRuleProperties) {
        this.retrofitResourceContext = retrofitResourceContext;
        this.cdiBeanManager = cdiBeanManager;
    }

    public void process() {
        List<RetrofitClientBean> retrofitClients = retrofitResourceContext.getRetrofitClients();
    }

    private void registFlowRules(List<RetrofitClientBean> retrofitClients) {

    }

    private void registDegradeRules(List<RetrofitClientBean> retrofitClients) {
    }

    private void setToDegradeRules(String name, String name1, Set<DegradeRuleBean> methodAllDegradeRules) {

    }

    private Set<DegradeRuleBean> getDegradeRuleBeansByAnnotation(Annotation[] annotations, boolean isMethod) {
        return null;
    }


}

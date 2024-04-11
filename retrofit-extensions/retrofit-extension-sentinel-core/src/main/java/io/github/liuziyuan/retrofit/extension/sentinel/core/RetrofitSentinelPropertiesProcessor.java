//package io.github.liuziyuan.retrofit.extension.sentinel.core;
//
//import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
//import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
//import io.github.liuziyuan.retrofit.core.CDIBeanManager;
//import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
//import io.github.liuziyuan.retrofit.core.resource.RetrofitApiServiceBean;
//import io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean;
//import io.github.liuziyuan.retrofit.extension.sentinel.core.annotation.RetrofitSentinelDegradeRule;
//import io.github.liuziyuan.retrofit.extension.sentinel.core.annotation.RetrofitSentinelDegrades;
//import io.github.liuziyuan.retrofit.extension.sentinel.core.properties.RetrofitSentinelDegradeRuleProperties;
//import io.github.liuziyuan.retrofit.extension.sentinel.core.properties.RetrofitSentinelFlowRuleProperties;
//import io.github.liuziyuan.retrofit.extension.sentinel.core.resource.DegradeRuleBean;
//import io.github.liuziyuan.retrofit.extension.sentinel.core.RetrofitSentinelResourceContext;
//import lombok.Getter;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Method;
//import java.util.*;
//
//public class RetrofitSentinelPropertiesProcessor {
//    @Getter
//    private Set<FlowRule> flowRules = new HashSet<>();
//    @Getter
//    private Set<DegradeRule> degradeRules = new HashSet<>();
//    @Getter
//    private RetrofitSentinelResourceContext sentinelResourceContext;
//
//    private final CDIBeanManager cdiBeanManager;
//
//    public RetrofitSentinelPropertiesProcessor(RetrofitResourceContext retrofitResourceContext,
//                                               CDIBeanManager cdiBeanManager,
//                                               RetrofitSentinelDegradeRuleProperties degradeRuleProperties,
//                                               RetrofitSentinelFlowRuleProperties flowRuleProperties) {
//        this.cdiBeanManager = cdiBeanManager;
//        this.sentinelResourceContext = new RetrofitSentinelResourceContext();
//        List<RetrofitClientBean> retrofitClients = retrofitResourceContext.getRetrofitClients();
//        setFlowRules(retrofitClients, flowRuleProperties);
//        setDegradeRules(retrofitClients, degradeRuleProperties);
//    }
//
//    private void setDegradeRules(List<RetrofitClientBean> retrofitClients, RetrofitSentinelDegradeRuleProperties degradeRuleProperties) {
//        for (RetrofitClientBean retrofitClient : retrofitClients) {
//            for (RetrofitApiServiceBean retrofitApiServiceBean : retrofitClient.getRetrofitApiServiceBeans()) {
//                degradeRuleProperties.getConfigs().forEach((k, v) -> {
//                    for (Class<?> interfaceClazz : v.getInterfaceClazz()) {
//                        if (interfaceClazz.isAssignableFrom(retrofitApiServiceBean.getSelfClazz())) {
//                            Class<?> selfClazz = retrofitApiServiceBean.getSelfClazz();
//                            Class<?> parentClazz = retrofitApiServiceBean.getParentClazz();
//
//                        }
//                    }
//                });
//            }
//        }
//    }
//
//    private void setFlowRules(List<RetrofitClientBean> retrofitClients, RetrofitSentinelFlowRuleProperties flowRuleProperties) {
//
//    }
//
//
//}

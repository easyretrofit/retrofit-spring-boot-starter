package io.github.liuziyuan.retrofit.extension.sentinel.spring.boot;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.extension.sentinel.core.RetrofitSentinelResourceProcessor;
import io.github.liuziyuan.retrofit.extension.sentinel.core.interceptor.SentinelBlockException;
import io.github.liuziyuan.retrofit.extension.sentinel.core.interceptor.SentinelBlockExceptionFallBackHandler;
import io.github.liuziyuan.retrofit.extension.sentinel.core.RetrofitSentinelResourceContext;
import io.github.liuziyuan.retrofit.extension.sentinel.spring.boot.config.RetrofitSpringSentinelDegradeRuleProperties;
import io.github.liuziyuan.retrofit.extension.sentinel.spring.boot.config.RetrofitSpringSentinelFlowRuleProperties;
import io.github.liuziyuan.retrofit.spring.boot.SpringCDIBeanManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
@EnableConfigurationProperties({RetrofitSpringSentinelFlowRuleProperties.class, RetrofitSpringSentinelDegradeRuleProperties.class})
public class RetrofitSentinelSpringBootConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 注册接口扩展
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public RetrofitSentinelInterceptorExtension retrofitSentinelInterceptorExtension() {
        return new RetrofitSentinelInterceptorExtension();
    }

    @Bean
    @ConditionalOnMissingBean
    public RetrofitSentinelResourceContext retrofitSentinelResourceContext(
            @Autowired RetrofitSpringSentinelDegradeRuleProperties degradeRuleProperties,
            @Autowired RetrofitSpringSentinelFlowRuleProperties flowRuleProperties) {
        RetrofitSentinelResourceProcessor processor = new RetrofitSentinelResourceProcessor(applicationContext.getBean(RetrofitResourceContext.class),
                new SpringCDIBeanManager(applicationContext), degradeRuleProperties, flowRuleProperties);
        RetrofitSentinelResourceContext context = processor.getSentinelResourceContext();
        context.check();
        FlowRuleManager.loadRules(new ArrayList<>(processor.getFlowRules()));
        DegradeRuleManager.loadRules(new ArrayList<>(processor.getDegradeRules()));
        return context;
    }

    @Bean
    @ConditionalOnMissingBean
    public SentinelBlockExceptionFallBackHandler exceptionFallBackHandler() {
        return new SentinelBlockExceptionFallBackHandler(SentinelBlockException.class, new SpringCDIBeanManager(applicationContext));
    }


}

package io.github.liuziyuan.retrofit.extension.sentinel.spring.boot;

import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.extension.sentinel.core.RetrofitSentinelAnnotationProcessor;
import io.github.liuziyuan.retrofit.extension.sentinel.core.interceptor.SentinelBlockException;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class RetrofitSentinelSpringBootConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        RetrofitResourceContext retrofitResourceContext = applicationContext.getBean(RetrofitResourceContext.class);
        RetrofitSentinelAnnotationProcessor processor = new RetrofitSentinelAnnotationProcessor(retrofitResourceContext);
        processor.process();
    }

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
    public SentinelBlockExceptionFallBackHandler exceptionFallBackHandler() {
        return new SentinelBlockExceptionFallBackHandler(SentinelBlockException.class);
    }

    /**
     * 注册Sentinel资源
     *
     * @return
     */
//    @Bean
//    @ConditionalOnMissingBean
//    public RetrofitSentinelAnnotationProcessor retrofitSentinelAnnotationProcessor() {
//        RetrofitSentinelAnnotationProcessor processor = new RetrofitSentinelAnnotationProcessor(applicationContext.getBean(RetrofitResourceContext.class));
//        processor.process();
//        return processor;
//    }


}

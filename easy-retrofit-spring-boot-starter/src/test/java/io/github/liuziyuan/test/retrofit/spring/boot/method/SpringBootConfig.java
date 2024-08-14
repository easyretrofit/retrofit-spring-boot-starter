package io.github.liuziyuan.test.retrofit.spring.boot.method;

import io.github.easyretrofit.core.RetrofitResourceContext;
import io.github.easyretrofit.core.resource.RetrofitApiServiceBean;
import io.github.easyretrofit.core.resource.RetrofitClientBean;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

@Configuration
public class SpringBootConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    private RetrofitResourceContext retrofitResourceContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        retrofitResourceContext = applicationContext.getBean(RetrofitResourceContext.class);
        for (RetrofitClientBean retrofitClient : retrofitResourceContext.getRetrofitClients()) {
            for (RetrofitApiServiceBean retrofitApiServiceBean : retrofitClient.getRetrofitApiServiceBeans()) {
                Class<?> clazz = retrofitApiServiceBean.getSelfClazz();
                Set<TestMethod> testMethods = new LinkedHashSet<>();
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    Annotation[] annotations = method.getDeclaredAnnotations();
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof TestMethods) {
                            TestMethod[] values = ((TestMethods) annotation).value();
                            Collections.addAll(testMethods, values);
                        } else if (annotation instanceof TestMethod) {
                            testMethods.add((TestMethod) annotation);
                        }
                    }
                }
            }
        }
    }
}

package io.github.liuziyuan.test.retrofit.spring.boot.inject.builder;

import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.core.resource.RetrofitBuilderBean;
import io.github.liuziyuan.retrofit.core.resource.RetrofitClientBean;
import io.github.liuziyuan.retrofit.spring.boot.EnableRetrofit;
import org.junit.Assert;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

/**
 * @author liuziyuan
 * @date 12/24/2021 5:51 PM
 */
@EnableRetrofit({"io.github.liuziyuan.test.retrofit.spring.boot.inject.builder"})
@SpringBootApplication
public class RetrofitSpringBootInjectBuilderApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(RetrofitSpringBootInjectBuilderApplication.class, args);
        BeanFactory beanFactory = run.getBeanFactory();
        RetrofitResourceContext bean = beanFactory.getBean(RetrofitResourceContext.class);
        List<RetrofitClientBean> retrofitClients = bean.getRetrofitClients();
        Assert.assertEquals(1, retrofitClients.size());
        RetrofitBuilderBean retrofitBuilder = retrofitClients.get(0).getRetrofitBuilder();
        Assert.assertEquals(1, retrofitBuilder.getAddCallAdapterFactory().length);
        Assert.assertNotNull(beanFactory.getBean(GsonConverterFactoryBuilder.class));
        Assert.assertNotNull(beanFactory.getBean(JacksonConverterFactoryBuilder.class));
        Assert.assertNotNull(beanFactory.getBean(MyCallBackExecutorBuilder.class));
        Assert.assertNotNull(beanFactory.getBean(MyOkHttpClient.class));
        Assert.assertNotNull(beanFactory.getBean(MyRetrofitInterceptor1.class));
        Assert.assertNotNull(beanFactory.getBean(MyRetrofitInterceptor3.class));
        Assert.assertThrows(NoSuchBeanDefinitionException.class, () -> beanFactory.getBean(MyRetrofitInterceptor2.class));
        Assert.assertNotNull(beanFactory.getBean(RxJavaCallAdapterFactoryBuilder.class));
        Assert.assertNotNull(beanFactory.getBean(MyRetrofitInterceptor1.class).getRetrofitResourceContext());
        Assert.assertNotNull(beanFactory.getBean(MyRetrofitInterceptor1.class).getContext());
    }
}

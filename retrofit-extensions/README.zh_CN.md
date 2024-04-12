# retrofit extensions

## 怎样创建一个retrofit的Interceptor扩展

easy retrofit 在设计之初，就考虑到了retrofit的Interceptor的扩展，在创建一个retrofit的Interceptor扩展时，需要如下步骤：

### 创建一个Annotation

这个Annotation用于标记一个类是一个Interceptor扩展。以 load balancer为例，代码如下：

```java

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@RetrofitDynamicBaseUrl
public @interface RetrofitLoadBalancer {
    @AliasFor(
            annotation = RetrofitDynamicBaseUrl.class,
            attribute = "value"
    )
    String name() default "";

    RetrofitInterceptor extensions() default @RetrofitInterceptor(handler = RetrofitLoadBalancerInterceptor.class);
}
```

需要强调的，必须要在Annotation中加上`RetrofitInterceptor extensions() default @RetrofitInterceptor(handler = RetrofitLoadBalancerInterceptor.class);`
其中handler的值就是这个Annotation对应的Interceptor。

### 创建一个Interceptor

创建一个Interceptor继承自`BaseInterceptor`，这里来实现你的逻辑。
以 load balancer为例，代码如下：

```java

@Component
public class RetrofitLoadBalancerInterceptor extends BaseInterceptor {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RetrofitResourceContext context;

    @SneakyThrows
    @Override
    protected Response executeIntercept(Chain chain) {
        Request request = chain.request();
        final Method method = super.getRequestMethod(request);
        String clazzName = super.getClazzNameByMethod(method);
        final RetrofitApiServiceBean currentServiceBean = context.getRetrofitApiServiceBean(clazzName);
        RetrofitLoadBalancer annotation = null;
        annotation = currentServiceBean.getSelfClazz().getAnnotation(RetrofitLoadBalancer.class);
        if (annotation == null) {
            annotation = currentServiceBean.getParentClazz().getAnnotation(RetrofitLoadBalancer.class);
        }
        final String serviceName = RetrofitUrlUtils.convertDollarPattern(annotation.name(), context.getEnv()::resolveRequiredPlaceholders);
        if (StringUtils.isNotEmpty(serviceName)) {
            final URI uri = loadBalancerClient.choose(serviceName).getUri();
            final HttpUrl httpUrl = HttpUrl.get(uri);
            HttpUrl newUrl = request.url().newBuilder()
                    .scheme(httpUrl.scheme())
                    .host(httpUrl.host())
                    .port(httpUrl.port())
                    .build();
            request = request.newBuilder().url(newUrl).build();
        }
        return chain.proceed(request);
    }
}
```

如果你使用了Spring 框架的`@Component`注解。那么需要注意在后面的步骤中，需要在`@Bean`中注册这个Interceptor。

### 注册Interceptor扩展

这是一个必须的步骤，用来注册这个Interceptor扩展到Retrofit中。 需要实现`RetrofitInterceptorExtension`接口
以 load balancer为例，代码如下：

```java
public class RetrofitLoadBalancerExtension implements RetrofitInterceptorExtension {
    @Override
    public Class<? extends Annotation> createAnnotation() {
        return RetrofitLoadBalancer.class;
    }

    @Override
    public Class<? extends BaseInterceptor> createInterceptor() {
        return RetrofitLoadBalancerInterceptor.class;
    }

    @Override
    public Class<? extends BaseExceptionDelegate<? extends RetrofitExtensionException>> createExceptionDelegate() {
        return null;
    }
}

```

### 注册到Spring容器

需要在Spring容器中注册这个RetrofitInterceptorExtension。
以 load balancer为例，代码如下：

```java

@Configuration
public class RetrofitLoadBalancerSpringCloudConfig {
    @Bean
    @ConditionalOnMissingBean
    public RetrofitLoadBalancerExtension retrofitSpringCouldWebConfig() {
        return new RetrofitLoadBalancerExtension();
    }

    @Bean
    @ConditionalOnMissingBean
    public RetrofitLoadBalancerInterceptor retrofitCloudInterceptor() {
        return new RetrofitLoadBalancerInterceptor();
    }
}
```

由于`RetrofitLoadBalancerInterceptor` 使用了Spring的注入功能，所以需要注入，如果拦截器中没有使用注入功能，那么并不是必须的。

### 配置Spring容器自动发现

需要再resources 文件夹中创建一个META-INF/spring.factories文件，内容如下：

```
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
io.github.liuziyuan.retrofit.extension.spring.cloud.loadbalancer.RetrofitLoadBalancerSpringCloudConfig
```

这个示例只支持SpringBoot2, 如需要SpringBoot3，请参考 load balancer项目的resources目录。

### 实现异常委托
这是一个可选的步骤，用来处理自定义扩展的异常。

这个委托是在运行时通过jdk 动态代理获取到异常，并将异常时方法的invoke传入委托中。当你创建自定义扩展时就可以获取到异常数据并后续处理异常。

我们可以在`RetrofitLoadBalancerExtension`中看到，如下代码：
```java
    @Override
    public Class<? extends BaseExceptionDelegate<? extends RetrofitExtensionException>> createExceptionDelegate() {
        return null;
    }
```
这个方法需要声明一个异常委托的类，用来处理异常。
#### 创建一个异常
创建一个异常，继承自`RetrofitExtensionException`，可以参考[SentinelBlockException.java](retrofit-extension-sentinel-core%2Fsrc%2Fmain%2Fjava%2Fio%2Fgithub%2Fliuziyuan%2Fretrofit%2Fextension%2Fsentinel%2Fcore%2Finterceptor%2FSentinelBlockException.java)


#### 创建一个异常委托
创建一个异常委托，继承自`BaseExceptionDelegate`， 可以参考[SentinelBlockExceptionFallBackHandler.java](retrofit-extension-sentinel-core%2Fsrc%2Fmain%2Fjava%2Fio%2Fgithub%2Fliuziyuan%2Fretrofit%2Fextension%2Fsentinel%2Fcore%2Finterceptor%2FSentinelBlockExceptionFallBackHandler.java)

#### 注册这个异常委托的类型
可以参考[RetrofitSentinelInterceptorExtension.java](retrofit-extension-sentinel-spring-boot-starter%2Fsrc%2Fmain%2Fjava%2Fio%2Fgithub%2Fliuziyuan%2Fretrofit%2Fextension%2Fsentinel%2Fspring%2Fboot%2FRetrofitSentinelInterceptorExtension.java)

#### 
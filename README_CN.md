# retrofit-spring-boot-starter

## 快速开始

前提条件：你已经掌握了Retrofit的基本用法

### 新增 retrofit-spring-boot-starter 依赖到maven pom.xml文件

```xml

<dependency>
    <groupId>io.github.liuziyuan</groupId>
    <artifactId>retrofit-spring-boot-starter</artifactId>
    <version>0.0.8</version>
</dependency>
```

### 新增 `@EnableRetrofit` 到你的Spring boot启动类

```java

@EnableRetrofit
@Slf4j
public class HelloApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(HelloApplication.class, args);
    }
}
```

You can specify basePackages like `@EnableRetrofit(basePackages = "xxx.demo.api")`, "xxx.demo.api" is your retrofit APIs
folder name. By default, all files in the directory where the starter class file is located will be scanned

您可以指定基本包，比如`@EnableRetrofit(basePackages = "xxx.demo.api")`，"xxx.demo.api"是您的Retrofit api
文件夹名。默认情况下，将扫描starter类文件所在目录中的所有文件

### 创建一个RetrofitConfig文件

```java

@Configuration
public class RetrofitConfig {
    @Bean
    public RetrofitResourceDefinitionRegistry retrofitResourceDefinitionRegistry() {
        return new RetrofitResourceDefinitionRegistry();
    }
}

```

### 创建一个接口文件, 并且使用`@RetrofitBuilder`

```java

@RetrofitBuilder(baseUrl = "${app.hello.url}")
public interface HelloApi {
    /**
     * call hello API method of backend service
     *
     * @param message message
     * @return
     */
    @GET("v1/hello/{message}")
    Call<ResponseBody> hello(@Path("message") String message);
}
```

和 application.yml

```yaml
app:
  hello:
    url: http://localhost:8080/
```

请保持`app.hello.url` 在你的资源配置文件中, baseUrl 也可以是一个URL像 `http://localhost:8080/`一样

### 使用 Retrofit API 在Controller

```java

@RestController
@RequestMapping("/v1/hello")
public class HelloController {

    @Autowired
    private HelloApi helloApi;

    @GetMapping("/{message}")
    public ResponseEntity<String> hello(@PathVariable String message) throws IOException {
        final ResponseBody body = helloApi.hello(message).execute().body();
        return ResponseEntity.ok(body.string());
    }
}
```

你可以参考 [retrofit-spring-boot-starter-sample-quickstart](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-quickstart)
& [retrofit-spring-boot-starter-sample-backend-services](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-backend-services)

### 是的，恭喜你，你的代码应该能正常工作。

## 高级用法

### 添加其他的Retrofit属性到 `@RetrofitBuilder`, 如果你需要

你可以在`@RetrofitBuilder`中设置Retrofit的其他属性， `@RetrofitBuilder`中的属性名称与`Retrofit.Builder`中的方法名是相同的

```java

@RetrofitBuilder(baseUrl = "${app.hello.url}",
        addConverterFactory = {GsonConvertFactoryBuilder.class},
        addCallAdapterFactory = {RxJavaCallAdapterFactoryBuilder.class},
        callbackExecutor = CallBackExecutorBuilder.class,
        client = OkHttpClientBuilder.class,
        validateEagerly = false)
@RetrofitInterceptor(handler = LoggingInterceptor.class)
@RetrofitInterceptor(handler = MyRetrofitInterceptor.class)
public interface HelloApi {
    /**
     * call hello API method of backend service
     *
     * @param message message
     * @return
     */
    @GET("v1/hello/{message}")
    Call<HelloBean> hello(@Path("message") String message);
}
```

### 创建自定义的 ConverterFactory

创建一个自定义的ConvertFactory类 需要继承`BaseConverterFactoryBuilder`类

```java
public class GsonConvertFactoryBuilder extends BaseConverterFactoryBuilder {
    @Override
    public Converter.Factory buildConverterFactory() {
        return GsonConverterFactory.create();
    }
}
```

### 创建自定义的 CallAdapterFactory

创建一个自定义的CallAdapterFactory类 需要继承`BaseCallAdapterFactoryBuilder`

```java
public class RxJavaCallAdapterFactoryBuilder extends BaseCallAdapterFactoryBuilder {
    @Override
    public CallAdapter.Factory buildCallAdapterFactory() {
        return RxJavaCallAdapterFactory.create();
    }
}
```

### 创建自定义的 CallBackExecutor

创建一个自定义的CallBackExecutor 需要继承 `BaseCallBackExecutorBuilder`

```java
public class CallBackExecutorBuilder extends BaseCallBackExecutorBuilder {

    @Override
    public Executor buildCallBackExecutor() {
        return command -> command.run();
    }
}
```

### 创建自定义的 OKHttpClient

创建一个自定义的OKHttpClient 需要继承 `BaseOkHttpClientBuilder`

```java
public class OkHttpClientBuilder extends BaseOkHttpClientBuilder {
    @Override
    public OkHttpClient.Builder buildOkHttpClientBuilder(OkHttpClient.Builder builder) {
        return builder.connectTimeout(Duration.ofMillis(30000));
    }
}
```

**重要事项:**
当需要在自定义的Builder中使用spring容器管理的对象时，只需在类头上使用`@Component`，并注入所需的对象

```java

@Component
public class MyOkHttpClient extends BaseOkHttpClientBuilder {

    @Value("${okhttpclient.timeout}")
    private int timeout;

    @Override
    public OkHttpClient.Builder buildOkHttpClientBuilder(OkHttpClient.Builder builder) {
        return builder.connectTimeout(Duration.ofMillis(timeout));
    }
}
```

```yaml
okhttpclient:
  timeout: 30000
```

### 创建自定义的 OKHttpClient Interceptor

Create a custom Interceptor of OKHttpClient need extend BaseInterceptor 创建一个自定义的 OKHttpClient Interceptor
需要继承`BaseInterceptor`

**重要事项:**
当需要在自定义的Builder中使用spring容器管理的对象时，只需在类头上使用`@Component`，并注入所需的对象

```java

@Component
public class MyRetrofitInterceptor extends BaseInterceptor {

    /**
     * The context is created and registered in the spring container by retrofit-spring-boot-starter. The context object includes all retrofit-spring-boot-starter context objects
     */
    @Autowired
    private RetrofitResourceContext context;

    @SneakyThrows
    @Override
    protected Response executeIntercept(Chain chain) {
        Request request = chain.request();
        String clazzName = Objects.requireNonNull(request.tag(Invocation.class)).method().getDeclaringClass().getName();
        final RetrofitServiceBean currentServiceBean = context.getRetrofitServiceBean(clazzName);
        // TODO if you need
        return chain.proceed(request);
    }
}
```

并且在接口名上声明如`@RetrofitInterceptor(handler = MyRetrofitInterceptor.class)`

### 使用 OkHttpClient HttpLoggingInterceptor

```java
public class LoggingInterceptor extends BaseInterceptor {

    private HttpLoggingInterceptor httpLoggingInterceptor;

    public LoggingInterceptor() {
        httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
    }

    @SneakyThrows
    @Override
    protected Response executeIntercept(Chain chain) {
        return httpLoggingInterceptor.intercept(chain);
    }
}
```

**小贴士:**

如果只想设置拦截器，而不想对OKHttpClient对象进行其他修改，则可以从`@RetrofitBuilder`删除`client = OkHttpClientBuilder.class`属性，无需自定义OKHttpClient

### 为@RetrofitInterceptor 设置 include,exclude,type 和 sort

你可以在`@RetrofitInterceptor` 上设置 `include`,`exclude`, `type` 和 `sort` 属性
like `@RetrofitInterceptor(handler = MyRetrofitInterceptor.class, exclude = {"/v1/hello/*"})`

当使用`exclude`, 相应的API将忽略此拦截器

当使用`sort`, 请确保所有拦截器都使用了sort属性,因为默认情况下,sort为0. 您可以通过int类型的值确保拦截器的执行顺序。**_默认的,拦截器从上到下装载。_**

当使用`type`, type是一个Interceptor的枚举类, 你可以指定此拦截器是OkHttpClient 的`addInterceptor()`方式或`addNetworkInterceptor()`方式

你可以参考 [retrofit-spring-boot-starter-sample-retrofitbuilder](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-retrofitbuilder)
&
[retrofit-spring-boot-starter-sample-backend-services](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-backend-services)

### 接口继承

如果你有成百上千个接口方法，它来自同一个HTTP URL源，你希望你的代码结构更有序，看起来与后台服务Controller结构一致，你可以这样做

#### 定义一个空的接口文件，并设置属性

```java

@RetrofitBuilder(baseUrl = "${app.hello.url}",
        addConverterFactory = {GsonConvertFactoryBuilder.class},
        client = OkHttpClientBuilder.class)
@RetrofitInterceptor(handler = LoggingInterceptor.class)
@RetrofitInterceptor(handler = MyRetrofitInterceptor.class)
public interface BaseApi {
}
```

#### 创建另一个接口文件继承父接口

```java
public interface HelloApi extends BaseApi {
    /**
     * call hello API method of backend service
     *
     * @param message message
     * @return
     */
    @GET("v1/hello/{message}")
    Call<HelloBean> hello(@Path("message") String message);
}
```

#### @RetrofitUrlPrefix 注解

你可以使用`@RetrofitUrlPrefix`去定义一个URL前缀，就像SpringBoot MVC中的`@RequestMapping`

```java

@RetrofitUrlPrefix("/v1/hello/")
public interface HelloApi extends BaseApi {
    /**
     * call hello API method of backend service
     *
     * @param message message
     * @return
     */
    @GET("{message}")
    Call<HelloBean> hello(@Path("message") String message);
}
```

```java
public interface HelloApi extends BaseApi {
    /**
     * call hello API method of backend service
     *
     * @param message message
     * @return
     */
    @GET("v1/hello/{message}")
    Call<HelloBean> hello(@Path("message") String message);
}
```

这两个`hello`方法具有相同的URL

你可以参考 [retrofit-spring-boot-starter-sample-inherit](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-inherit)
& [retrofit-spring-boot-starter-sample-backend-services](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-backend-services)

**Warning:**
如果在同一Controller注入父接口和继承接口，可能会发生以下错误

```
Description:

Field api in io.liuziyuan.demo.controller.HelloController required a single bean, but 2 were found:
	- io.github.liuziyuan.retrofit.samples.inherit.api.BaseApi: defined in null
	- io.github.liuziyuan.retrofit.samples.inherit.api.HelloApi: defined in null

Action:

Consider marking one of the beans as @Primary, updating the consumer to accept multiple beans, or using @Qualifier to identify the bean that should be consumed
```

所以，需要使用@Qualifier(""),其值为API接口类全名, 请尽量不要在此处使用父类

```java

@RestController
@RequestMapping("/v1/hello")
public class HelloController {

    @Autowired
    @Qualifier("io.github.liuziyuan.retrofit.samples.inherit.api.BaseApi")
    private BaseApi baseApi;
    @Autowired
    @Qualifier("io.github.liuziyuan.retrofit.samples.inherit.api.helloApi")
    private HelloApi helloApi;

    @GetMapping("/{message}")
    public ResponseEntity<String> hello(@PathVariable String message) throws IOException {
        final HelloBean helloBody = helloApi.hello(message).execute().body();
        return ResponseEntity.ok(helloBody.getMessage());
    }
}
```

### 单Retrofit实例

当Retrofit配置相同且仅为`baseUrl`的后缀部分不同时，会只创建一个Retrofit实例

你可以参考 [retrofit-spring-boot-starter-sample-single-instance](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-single-instance)
& [retrofit-spring-boot-starter-sample-backend-services](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-backend-services)

### @RetrofitDynamicBaseUrl 注解

你可以使用`@RetrofitDynamicBaseUrl` 动态的改变`@RetrofitBuilder`中的`baseUrl`

你可以参考 [retrofit-spring-boot-starter-sample-awesome](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-awesome)
& [retrofit-spring-boot-starter-sample-backend-services](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-backend-services)

## 为什么这里会有另一个 retrofit-spring-boot-starter

首先感谢[lianjiatech](https://github.com/LianjiaTech/retrofit-spring-boot-starter)
提供一个近乎完美的项目[retrofit-spring-boot-starter](https://github.com/LianjiaTech/retrofit-spring-boot-starter).
但是,在使用中,我发现它会为每个API Interface 文件创建一个 retrofit 实例,在我看来这是一个资源浪费. 看完代码,我觉得很难在短时间内修改原来的代码,所以我重复造了一个轮子. 在我的工作中，团队将使用Retrofit
作为BFF层的HTTP客户端请求微服务,因此,有成败上线个API接口文件在BFF项目中. 因此，我改进了创建Retrofit实例的时间，允许一个Retrofit API接口继承一个基本接口，它可以定义和配置Retrofit属性


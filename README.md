# retrofit-spring-boot-starter

## Why is there another retrofit-spring-boot-starter

First of all, thank [lianjiatech](https://github.com/LianjiaTech/retrofit-spring-boot-starter) for providing an almost
perfect project of [retrofit-spring-boot-starter](https://github.com/LianjiaTech/retrofit-spring-boot-starter).

However, in use, I found that it will create a retrofit instance for each API Interface file, which in my opinion is a
waste of resources. After reading the code, I think it is difficult to modify the original basis in a short time, so I
repeated a wheel.

In my work, the team will use retrofit as the API of BFF layer HTTP client to request micro services. Therefore, there
will be hundreds of interface files in BFF. Therefore, I improved the time of creating retrofit instance, allowing one
retrofit interface to inherit one base interface, which can define and configure retrofit attributes

You can see the effect I want from the fourth step of introduction

## How to use it

Pre-conditions: you have mastered the basic usage of retrofit

### Add retrofit-spring-boot-starter dependency to maven pom.xml

```xml

<dependency>
    <groupId>io.github.liuziyuan</groupId>
    <artifactId>retrofit-spring-boot-starter</artifactId>
    <version>0.0.6</version>
</dependency>
```

### Add `@EnableRetrofit` to your Spring boot starter Class

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

### Create a RetrofitConfig file

```java

@Configuration
public class RetrofitConfig {
    @Bean
    public RetrofitResourceDefinitionRegistry retrofitResourceDefinitionRegistry() {
        return new RetrofitResourceDefinitionRegistry();
    }
}

```

### Create an Interface file, and use `@RetrofitBuilder`

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

and application.yml

```yaml
app:
  hello:
    url: http://localhost:8080/
```

Pls keep app.hello.url on your resources' config file, baseUrl can also be a URL as `http://localhost:8080/`

### Use Retrofit API in Controller

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

You can refer
to [retrofit-spring-boot-starter-sample-quickstart](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-quickstart)
& [retrofit-spring-boot-starter-sample-backend-services](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-backend-services)

### Yes, Congratulations, your code should work normally.

## Advanced usage

### Add other Retrofit attributes to `@RetrofitBuilder`, if you need

You can set the other properties of Retrofit in @RetrofitBuilder, the `@RetrofitBuilder` properties name is same as
method name of `Retrofit.Builder`

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

### Create custom ConverterFactory

Create a custom ConvertFactory extends BaseConverterFactoryBuilder

```java
public class GsonConvertFactoryBuilder extends BaseConverterFactoryBuilder {
    @Override
    public Converter.Factory buildConverterFactory() {
        return GsonConverterFactory.create();
    }
}
```

### Create custom CallAdapterFactory

Create a custom CallBackExecutor extends BaseCallBackExecutorBuilder

```java
public class CallBackExecutorBuilder extends BaseCallBackExecutorBuilder {

    @Override
    public Executor buildCallBackExecutor() {
        return command -> command.run();
    }
}
```

### Create custom CallBackExecutor

Create a custom CallAdapterFactory extends BaseCallAdapterFactoryBuilder

```java
public class RxJavaCallAdapterFactoryBuilder extends BaseCallAdapterFactoryBuilder {
    @Override
    public CallAdapter.Factory buildCallAdapterFactory() {
        return RxJavaCallAdapterFactory.create();
    }
}
```

### Create custom OKHttpClient

Create a custom OKHttpClient need extends BaseOkHttpClientBuilder

```java
public class OkHttpClientBuilder extends BaseOkHttpClientBuilder {
    @Override
    public OkHttpClient.Builder buildOkHttpClientBuilder(OkHttpClient.Builder builder) {
        return builder.connectTimeout(Duration.ofMillis(30000));
    }
}
```

**important:**
When you need to use the objects managed by the spring container in the Custom Builder, you only need to
use `@Component` on the class header and inject the objects you need

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

### Create custom OKHttpClient Interceptor

Create a custom Interceptor of OKHttpClient extends BaseInterceptor

**important:**
When you need to use the objects managed by the spring container in the Custom Interceptor, you only need to
use `@Component` on the class header and inject the objects you need

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

and set class name like `@RetrofitInterceptor(handler = MyRetrofitInterceptor.class)  `

### Use OkHttpClient HttpLoggingInterceptor

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

**Tips:**
When you only want to set the interceptor without making other modifications to the OKHttpClient object, you can delete
the `client = OkHttpClientBuilder.class` property of `@RetrofitBuilder` and There is no need to customize your
OKHttpClient

### Set include,exclude,type and sort for @RetrofitInterceptor

you could set `include`,`exclude`, `type` and `sort` properties in @RetrofitInterceptor
like `@RetrofitInterceptor(handler = MyRetrofitInterceptor.class, exclude = {"/v1/hello/*"})`

When `exclude` is used, the corresponding API will ignore this interceptor.

When you use `sort`, please ensure that all interceptors use sort, because by default, sort is 0. You can ensure the
execution order of your interceptors through int type. **_By default, the interceptor is loaded from top to bottom._**

When you use `type`, type is an Interceptor Enum , You can specify whether this interceptor is to be `addInterceptor()`
or `addNetworkInterceptor()` in OkHttpClient

You can refer
to [retrofit-spring-boot-starter-sample-retrofitbuilder](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-retrofitbuilder)
&
[retrofit-spring-boot-starter-sample-backend-services](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-backend-services)

### Interface inheritance

If you have hundreds of Interface method, it is from a same source Base URL, and you want your code structure to be more
orderly and look consistent with the source service structure, you could do this,

#### Define an empty Interface file

```java

@RetrofitBuilder(baseUrl = "${app.hello.url}",
        addConverterFactory = {GsonConvertFactoryBuilder.class},
        client = OkHttpClientBuilder.class)
@RetrofitInterceptor(handler = LoggingInterceptor.class)
@RetrofitInterceptor(handler = MyRetrofitInterceptor.class)
public interface BaseApi {
}
```

#### Create other API Interface extend Parent class

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

Please try not to use the parent class in the injected place

You can refer
to [retrofit-spring-boot-starter-sample-inherit](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-inherit)
& [retrofit-spring-boot-starter-sample-backend-services](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-backend-services)

**Warning:**
If you inject the parent Interface and the inherited Interface at the same place, the following errors may occur

```
Description:

Field api in io.liuziyuan.demo.controller.HelloController required a single bean, but 2 were found:
	- io.github.liuziyuan.retrofit.samples.inherit.api.BaseApi: defined in null
	- io.github.liuziyuan.retrofit.samples.inherit.api.HelloApi: defined in null

Action:

Consider marking one of the beans as @Primary, updating the consumer to accept multiple beans, or using @Qualifier to identify the bean that should be consumed
```

So, you need use @Qualifier(""), and the value is API Interface class full name

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

### Single Retrofit instance

Create a single Retrofit instance When the Retrofit configuration is the same and only the prefix part of the `baseUrl`
is different

You can refer
to [retrofit-spring-boot-starter-sample-single-instance](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-single-instance)
& [retrofit-spring-boot-starter-sample-backend-services](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-backend-services)
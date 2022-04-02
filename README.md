# retrofit-spring-boot-starter

**retrofit-spring-boot-starter** provides easier use and enhancement of common functions of **Retrofit** in **SpringBoot**
project, and realizes the enhancement of common functions through more annotations.

_Feature_

1. **Single Instance**, When the properties of the `Retrofit.Builder()` object are the same, only **one instance** of
   Retrofit will be generated.
2. **Interface inheritance**,When there are many Retrofit APIs in the project, the API interface file can be decomposed
   more structurally through Interface inheritance
3. **DynamicURL**, When using `@RetrofitDynamicBaseUrl` annotation, the entire API interface file uses dynamic base url,
   while other API interface files are not affected

## Table of contents

- [Quick Start](#quick-start)
- [Advanced Usage](#advanced-usage)
- [Interface Inheritance](#interface-inheritance)
- [Single Retrofit Instance](#single-retrofit-instance)

## Quick Start

### Add retrofit-spring-boot-starter dependency to maven pom.xml

```xml

<dependency>
    <groupId>io.github.liuziyuan</groupId>
    <artifactId>retrofit-spring-boot-starter</artifactId>
    <version>0.0.18</version>
</dependency>
```

### Add `@EnableRetrofit` Annotation

The `@EnableRetrofit` Annotation will enable to use retrofit-spring-boot-stater.

```java

@EnableRetrofit
@SpringBootApplication
public class QuickStartApplication {
   public static void main(String[] args) {
      SpringApplication.run(QuickStartApplication.class, args);
   }
}

```

You can specify basePackages like `@EnableRetrofit(basePackages = "xxx.demo.api")`, "xxx.demo.api" is your retrofit APIs
folder name. By default, all files in the directory where the starter class file is located will be scanned

### Create an Interface file, and use `@RetrofitBuilder`

`@RetrofitBuilder` will create a `Retrofit.Builder()` object, and it will be managed by Spring container

`baseUrl` can be a URL string or a properties in a resource file

```java

// baseUrl = "http://localhost:8080/"
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

### Use Retrofit API in Controller

Use `@Autowired` to inject API Interface, the retrofit-spring-boot-starter will help you to create instance of API
Interface file.

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

## Advanced Usage

### Add other Retrofit attributes to `@RetrofitBuilder`, if you need

You can set the other properties of Retrofit in @RetrofitBuilder, the `@RetrofitBuilder` properties name is same as
method name of `Retrofit.Builder()`

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

Create a custom ConvertFactory need extend `BaseConverterFactoryBuilder`

```java
public class GsonConvertFactoryBuilder extends BaseConverterFactoryBuilder {
    @Override
    public Converter.Factory buildConverterFactory() {
        return GsonConverterFactory.create();
    }
}
```

### Create custom CallAdapterFactory

Create a custom CallAdapterFactory need extend `BaseCallAdapterFactoryBuilder`

```java
public class RxJavaCallAdapterFactoryBuilder extends BaseCallAdapterFactoryBuilder {
    @Override
    public CallAdapter.Factory buildCallAdapterFactory() {
        return RxJavaCallAdapterFactory.create();
    }
}
```

### Create custom CallBackExecutor

Create a custom CallBackExecutor need extend `BaseCallBackExecutorBuilder`

```java
public class CallBackExecutorBuilder extends BaseCallBackExecutorBuilder {

    @Override
    public Executor buildCallBackExecutor() {
        return command -> command.run();
    }
}
```

### Create custom OKHttpClient

Create a custom OKHttpClient need extend `BaseOkHttpClientBuilder`

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

and application.yml

```yaml
okhttpclient:
  timeout: 30000
```

### Create custom OKHttpClient Interceptor

The OkHttpClient Interceptor object can be created separately from the OkHttpClient object, which makes it more flexible
to expand and use

Create a custom Interceptor of OKHttpClient need extend BaseInterceptor

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

## Interface Inheritance

If you have hundreds of Interface method, it is from a same source Base URL, and you want your code structure to be more
orderly and look consistent with the source service structure, you could do this,

### Interface file `extends` Mode

Use `extends` keyword to inherit the interface that declares @RetrofitBuilder

#### Define an empty Interface file,and set `@RetrofitBuilder`

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

### `@RetrofitBase` mode

Use @RetrofitBase annotation to set @RetrofitBuilder Interface file

```java

@RetrofitBase(baseInterface = BaseApi.class)
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

if `HelloApi` use `extends BaseApi` and used `@RetrofitBase(baseInterface = BaseApi.class)`, The starter first to use `@RetrofitBase(baseInterface = BaseApi.class)`

#### @RetrofitUrlPrefix Annotation

You can use `@RetrofitUrlPrefix` to define the prefix of URL, just like using `@RequestMapping` of springboot

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

The URLs of the two `hello` methods are the same

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

So, you need use @Qualifier(""), and the value is API Interface class full name, Please try not to use the parent class
in the injected place

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

### Single Retrofit Instance

Create a single Retrofit instance When the Retrofit configuration is the same and only the SUFFIX part of the `baseUrl`
is different

You can refer
to [retrofit-spring-boot-starter-sample-single-instance](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-single-instance)
& [retrofit-spring-boot-starter-sample-backend-services](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-backend-services)

### @RetrofitDynamicBaseUrl Annotation

You can use `@RetrofitDynamicBaseUrl` to dynamically change the `baseUrl` in `@RetrofitBuilder`

You can refer
to [retrofit-spring-boot-starter-sample-awesome](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-awesome)
& [retrofit-spring-boot-starter-sample-backend-services](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-backend-services)

## Why is there another retrofit-spring-boot-starter

First, thank [lianjiatech](https://github.com/LianjiaTech/retrofit-spring-boot-starter) for providing an almost perfect
project of [retrofit-spring-boot-starter](https://github.com/LianjiaTech/retrofit-spring-boot-starter).

However, in use, I found that it will create a retrofit instance for each API Interface file, which in my opinion is a
waste of resources. After reading the code, I think it is difficult to modify the original basis in a short time, so I
repeated a wheel.

In my work, the team will use retrofit as the API of BFF layer HTTP client to request micro services. Therefore, there
will be hundreds of interface files in BFF. Therefore, I improved the time of creating retrofit instance, allowing one
retrofit interface to inherit one base interface, which can define and configure retrofit attributes


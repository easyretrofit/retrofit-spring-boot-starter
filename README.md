[![Version](https://img.shields.io/maven-central/v/io.github.easyretrofit/spring-boot-starter?logo=apache-maven&style=flat-square)](https://central.sonatype.com/artifact/io.github.easyretrofit/spring-boot-starter)
[![Build](https://github.com/easyretrofit/spring-boot-starter/actions/workflows/build.yml/badge.svg)](https://github.com/easyretrofit/spring-boot-starter/actions/workflows/build.yml/badge.svg)
[![License](https://img.shields.io/github/license/easyretrofit/spring-boot-starter.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![License](https://img.shields.io/badge/JDK-8+-4EB1BA.svg)](https://docs.oracle.com/javase/8/)
[![License](https://img.shields.io/badge/spring--boot-2.0.0+-green.svg)]()

# easy-retrofit-spring-boot-starter

[中文文档](https://github.com/liuziyuan/retrofit-spring-boot-starter/blob/main/README.zh_CN.md)

## Table of contents

- [Quick Start](#quick-start)
- [Advanced Configuration](#advanced-configuration)
- [Interface Inheritance](#interface-inheritance)
- [URL Prefix](#url-prefix)
- [Single Retrofit Instance](#single-retrofit-instance)
- [Dynamic URL](#dynamic-url)
- [Global Configuration](#global-configuration)
- [Interceptor Extension](#interceptor-extension)
- [Why is there another retrofit-spring-boot-starter](#why-is-there-another-retrofit-spring-boot-starter)
- [Maintainers](#maintainers)
- [Contributing](#contributing)
- [License](#license)

## Introduction
`easy-retrofit-spring-boot-starter` provides easier use and enhancement of common functions of Retrofit2 in SpringBoot project, and realizes the enhancement of common functions through more annotations.

## Install

**Maven:**

```xml

<dependency>
    <groupId>io.github.easyretrofit</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <version>${latest-version}</version>
</dependency>
```

**Gradle:**

```groovy
dependencies {
    implementation 'io.github.easyretrofit:spring-boot-starter:${latest-version}'
}
```

This version is compatible with SpringBoot2 and Springboot3 versions under different JDKs as follows:

| jdk version | Springboot2 version   | Springboot3 version |
|-------------|-----------------------|---------------------|
| jdk8        | 2.0.0.RELEASE - 2.7.x | NA                  |
| jdk11       | 2.0.0.RELEASE - 2.7.x | NA                  |
| jdk17       | 2.4.2 - 2.7.x         | 3.0.0 - latest      |
| jdk21       | 2.7.16 - 2.7.x        | 3.1.0 - latest      |


## Quick Start

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

## Advanced Configuration

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

if `HelloApi` use `extends BaseApi` and used `@RetrofitBase(baseInterface = BaseApi.class)`, The starter first to
use `@RetrofitBase(baseInterface = BaseApi.class)`

## URL Prefix

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
	- io.github.easyretrofit.samples.inherit.api.BaseApi: defined in null
	- io.github.easyretrofit.samples.inherit.api.HelloApi: defined in null

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
    @Qualifier("io.github.easyretrofit.samples.inherit.api.BaseApi")
    private BaseApi baseApi;
    @Autowired
    @Qualifier("io.github.easyretrofit.samples.inherit.api.helloApi")
    private HelloApi helloApi;

    @GetMapping("/{message}")
    public ResponseEntity<String> hello(@PathVariable String message) throws IOException {
        final HelloBean helloBody = helloApi.hello(message).execute().body();
        return ResponseEntity.ok(helloBody.getMessage());
    }
}
```

## Single Retrofit Instance

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

### @RetrofitCloudService Annotation (v0.0.20 temporary discard)

In spring cloud micro services cluster, You can use `@RetrofitCloudService` to call another micro service.
This function depends on `spring-cloud-starter-loadbalancer`,so adding to pom.xml

```xml

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-loadbalancer</artifactId>
</dependency>
```

```java

@RetrofitBuilder
@RetrofitCloudService(name = "catalog")
public interface RetrofitApi {

    @GET("echo/{string}")
    Call<ResponseBody> echo(@Path("string") String string);
}
```

the `catalog` is name of provider micro service. That's the name in the registry center.

## Dynamic URL

you could use`@RetrofitDynamicBaseUrl` dynamic change `baseUrl`of`@RetrofitBuilder`

You can refer to  [retrofit-spring-boot-starter-sample-awesome](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-awesome)
& [retrofit-spring-boot-starter-sample-backend-services](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-backend-services)

## Global Configuration

You can configure the `retrofit-spring-boot-starter` global configuration in the Spring Boot project's configuration file `application.yml`. 
When global configuration is enabled, all the configurations in the `@RetrofitBuilder` will use the global configuration.

```yaml
retrofit:
  global:
    enable: true
    overwrite-type: global_first
    base-url: http://localhost:8080
    converter-factory-builder-clazz: io.github.liuziyuan.test.retrofit.spring.boot.common.JacksonConverterFactoryBuilder,io.github.liuziyuan.test.retrofit.spring.boot.common.GsonConverterFactoryBuilder
    call-adapter-factory-builder-clazz: io.github.liuziyuan.test.retrofit.spring.boot.common.RxJavaCallAdapterFactoryBuilder
    validate-eagerly: false
```

The properties here are exactly the same as those in `@RetrofitBuilder`. You can refer to the comments in `@RetrofitBuilder` for explanation.

The `overwrite-type` provides two modes: `global_first` and `local_first`.

When `global_first`, the global configuration will be merged with the `@RetrofitBuilder` configuration and use the properties of the global configuration. The properties of the global configuration will be used if they are empty.

When `local_first`, the global configuration will be merged with the `@RetrofitBuilder` configuration and use the properties of the `@RetrofitBuilder` configuration. The properties of the `@RetrofitBuilder` configuration will be used if they are empty.

Provide a `denyGlobalConfig = true` in `@RetrofitBuilder` to refuse the global configuration and keep its independence without being polluted by the global configuration.

```java

@RetrofitBuilder(baseUrl = "http://localhost:8080/", denyGlobalConfig = true)
public interface HelloApi {
}
```

You can refer to [retrofit-spring-boot-starter-sample-global-config](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-global-config)

## Interceptor Extension

You can write Interceptor-based extensions for Retrofit,

Take for example `@RetrofitCloudService`, an Interceptor-based extension that implements load balancing for Retrofit in Spring Cloud
```java

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@RetrofitDynamicBaseUrl
@RetrofitInterceptor(handler = RetrofitCloudInterceptor.class)
public @interface RetrofitCloudService {
    @AliasFor(
            annotation = RetrofitDynamicBaseUrl.class,
            attribute = "value"
    )
    String name() default "";
}

@Component
public class RetrofitCloudInterceptor extends BaseInterceptor {

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
        final RetrofitApiInterfaceBean currentServiceBean = context.getRetrofitApiInterfaceBean(clazzName);
        RetrofitCloudService annotation = null;
        annotation = currentServiceBean.getSelfClazz().getAnnotation(RetrofitCloudService.class);
        if (annotation == null) {
            annotation = currentServiceBean.getParentClazz().getAnnotation(RetrofitCloudService.class);
        }
        final String serviceName = annotation.name();
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

public class RetrofitCloudServiceExtension implements RetrofitInterceptorExtension {
    @Override
    public Class<? extends Annotation> createAnnotation() {
        return RetrofitCloudService.class;
    }

    @Override
    public Class<? extends BaseInterceptor> createInterceptor() {
        return RetrofitCloudInterceptor.class;
    }
}


@Configuration
public class RetrofitSpringCouldWebConfig {
    @Bean
    @ConditionalOnMissingBean
    public RetrofitCloudServiceExtension retrofitSpringCouldWebConfig() {
        return new RetrofitCloudServiceExtension();
    }

    @Bean
    @ConditionalOnMissingBean
    public RetrofitCloudInterceptor retrofitCloudInterceptor() {
        return new RetrofitCloudInterceptor();
    }
}


@RetrofitBuilder
@RetrofitCloudService(name = "catalog")
public interface RetrofitApi {

    @GET("echo/{string}")
    Call<ResponseBody> echo(@Path("string") String string);
}

```

You can refer to [retrofit-spring-boot-starter-sample-plugin](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-plugin)


## Why is there another retrofit-spring-boot-starter

First, thank [lianjiatech](https://github.com/LianjiaTech/retrofit-spring-boot-starter) for providing an almost perfect
project of [retrofit-spring-boot-starter](https://github.com/LianjiaTech/retrofit-spring-boot-starter).

However, in use, I found that it will create a retrofit instance for each API Interface file, which in my opinion is a
waste of resources. After reading the code, I think it is difficult to modify the original basis in a short time, so I
repeated a wheel.

In my work, the team will use retrofit as the API of BFF layer HTTP client to request micro services. Therefore, there
will be hundreds of interface files in BFF. Therefore, I improved the time of creating retrofit instance, allowing one
retrofit interface to inherit one base interface, which can define and configure retrofit attributes

## Maintainers

[@liuziyuan](https://github.com/liuziyuan)

## Contributing

Feel free to dive in! [Open an issue](https://github.com/liuziyuan/retrofit-spring-boot-starter/issues/new) or submit
PRs.

Standard Readme follows the [Contributor Covenant](http://contributor-covenant.org/version/1/3/0/) Code of Conduct.

This project exists thanks to all the people who contribute.

## License

[MIT](LICENSE) © liuziyuan
# retrofit-spring-boot-starter

## 目录
- [介绍](#介绍)
- [安装](#安装)
- [快速开始](#快速开始)
- [更多设置](#更多设置)
- [接口继承](#接口继承)
- [URL前缀](#URL前缀)
- [单Retrofit实例](#单Retrofit实例)
- [动态URL](#动态URL)
- [全局配置功能](#全局配置功能)
- [插件扩展功能](#插件扩展功能)
- [为什么这里会有另一个 retrofit-spring-boot-starter](#为什么这里会有另一个 retrofit-spring-boot-starter)
- [Maintainers](#maintainers)
- [Contributing](#contributing)
- [License](#license)

## 介绍
在SpringBoot项目中快速使用Retrofit2,`retrofit-spring-boot-starter`提供了一个基于注解的配置创建Retrofit实例，并通过更多的注释实现了通用功能的增强。
本项目使用Springboot的动态代理机制，在Spring上下文的生命周期前期，将Retrofit实例注入到Spring上下文。

## 安装
**Maven:**
```xml

<dependency>
    <groupId>io.github.liuziyuan</groupId>
    <artifactId>retrofit-spring-boot-starter</artifactId>
    <version>${latest-version}</version>
</dependency>
```
**Gradle:**
```groovy
dependencies {
    implementation 'io.github.liuziyuan:retrofit-spring-boot-starter:${latest-version}'
}
```
本项目的最多兼容稳定版本号为 `1.3.1`,以及后续版本。该版本在不同JDK下兼容SpringBoot2和Springboot3版本如下:

| jdk version | Springboot2 version    | Springboot3 version |
|-------------|------------------------|---------------------|
| jdk8        | 2.0.0.RELEASE - 2.7.18 | NA                  |
| jdk11       | 2.0.0.RELEASE - 2.7.18 | NA                  |
| jdk17       | 2.4.2 - 2.7.18         | 3.0.0 - latest      |
| jdk21       | 2.7.16 - 2.7.18        | 3.1.0 - latest(*)   |

注意：如果你需要在jdk21的Springboot3.1.0-3.1.2中使用Lombok，请注意lombok的版本号为1.18.30 +
```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.30</version>
    <optional>true</optional>
</dependency>
```

## 快速开始

_前提条件：你已经掌握了Retrofit的基本用法_

如下代码展示了在在传统Retrofit2的使用中，GitHubService接口需要被显示的创建。
```java
public interface GitHubService {
  @GET("users/{user}/repos")
  Call<List<Repo>> listRepos(@Path("user") String user);
}


Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .build();

GitHubService service = retrofit.create(GitHubService.class);
```
在传统Retrofit2的使用中，GitHubService接口需要被显示的创建。

通过`retrofit-spring-boot-starter`, `GitHubService`不再需要显示的创建,而是被Spring容器动态代理创建。

如下展示了如何使用`retrofit-spring-boot-starter`快速使用Retrofit2的方式。

### 添加 `@EnableRetrofit`
在Springboot的启动类上增加`@EnableRetrofit`注解，代表可以开启Retrofit2的自动配置。
```java

@EnableRetrofit
@SpringBootApplication
public class QuickStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuickStartApplication.class, args);
    }
}

```

您可以指定basePackages，比如`@EnableRetrofit(basePackages = "xxx.demo.api")`，"xxx.demo.api"是您的Retrofit api
文件夹名。默认情况下，将扫描starter类文件所在目录中的所有文件

### 创建一个接口文件, 并且使用`@RetrofitBuilder`
`@RetrofitBuilder` 注解可以用来配置Retrofit2的配置信息，如baseUrl等。他完整的代替了显示的`Retrofit.Builder()`来创建Retrofit实例。
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

和 `application.yml`

```yaml
app:
  hello:
    url: http://localhost:8080/
```

请保持`app.hello.url` 在你的资源配置文件中, baseUrl 也可以是一个URL, 就像 `http://localhost:8080/` 一样。

当然你也可以直接在写成`@RetrofitBuilder(baseUrl = "http://localhost:8080/")`。

### 注入 Retrofit API 接口
可以在Spring的Controller中注入Retrofit API接口。
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

**是的，恭喜你，你的代码应该能正常工作。**

## 更多设置

### 添加其他的Retrofit属性到 `@RetrofitBuilder`, 如果你需要

你可以在`@RetrofitBuilder`中设置Retrofit的其他属性， `@RetrofitBuilder`中的属性名称与`Retrofit.Builder()`中的方法名是相同的

你可以按需的添加其他的Retrofit属性到 `@RetrofitBuilder`

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

**_重要事项_:**

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

创建一个自定义的 OKHttpClient Interceptor 需要继承`BaseInterceptor`，并且在接口名上声明如`@RetrofitInterceptor(handler = MyRetrofitInterceptor.class)`

```java

@RetrofitBuilder(baseUrl = "${app.hello.url}",
        addConverterFactory = {GsonConvertFactoryBuilder.class},
        client = OkHttpClientBuilder.class)
@RetrofitInterceptor(handler = LoggingInterceptor.class)
@RetrofitInterceptor(handler = MyRetrofitInterceptor.class)
public interface BaseApi {
}
```

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

_**重要事项:**_

当需要在自定义的Builder中使用spring容器管理的对象时，只需在类头上使用`@Component`，并注入所需的对象

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

_**小贴士:**_

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

## 接口继承

如果你有成百上千个接口方法，它来自同一个HTTP URL源，你希望你的代码结构更有序，看起来与后台服务Controller结构一致，你可以这样做

### 接口文件 `extends` 模式

使用 `extends` 关键字来继承声明RetrofitBuilder的接口

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

### `@RetrofitBase` 模式

使用`@RetrofitBase` 来设置有`@RetrofitBuilder`的接口

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

如果 `HelloApi` 使用 `extends BaseApi` 并且使用了 `@RetrofitBase(baseInterface = BaseApi.class)`, starter会优先去使用 `@RetrofitBase(baseInterface = BaseApi.class)`

## URL前缀

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

**在运行中，这两个接口会生成相同的URL `/v1/hello/{message}`**

你可以参考 [retrofit-spring-boot-starter-sample-inherit](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-inherit)
& [retrofit-spring-boot-starter-sample-backend-services](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-backend-services)

_**Warning:**_
如果在同一Controller注入父接口和子接口，可能会发生以下错误

```
Description:

Field api in io.liuziyuan.demo.controller.HelloController required a single bean, but 2 were found:
	- io.github.liuziyuan.retrofit.samples.inherit.api.BaseApi: defined in null
	- io.github.liuziyuan.retrofit.samples.inherit.api.HelloApi: defined in null

Action:

Consider marking one of the beans as @Primary, updating the consumer to accept multiple beans, or using @Qualifier to identify the bean that should be consumed
```

所以，需要使用@Qualifier(""),其值为API接口类全名, 请尽量不要混用父接口与子接口

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

## 单Retrofit实例

当Retrofit配置相同且仅为`baseUrl`的后缀部分不同时，会只创建一个Retrofit实例。

在`retrofit-spring-boot-starter`中可以看到,一个retrofit实例被称之为`RetrofitClient`，一个Retrofit实例的接口被称之为`RetrofitApiService`。

你可以通过Demo中的Log查看到RetrofitClient和RetrofitApiService之间的关系。

通过这样的方式，可以减少Retrofit实例的数量，降低内存占用。

你可以参考 [retrofit-spring-boot-starter-sample-single-instance](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-single-instance)
& [retrofit-spring-boot-starter-sample-backend-services](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-backend-services)

## 动态URL

你可以使用`@RetrofitDynamicBaseUrl` 动态的改变`@RetrofitBuilder`中的`baseUrl`

你可以参考 [retrofit-spring-boot-starter-sample-awesome](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-awesome)
& [retrofit-spring-boot-starter-sample-backend-services](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-backend-services)

## 全局配置功能
你可以在Springboot项目中的配置文件中application.yml中配置`retrofit-spring-boot-starter`的全局配置,当开启全局配置后，所有的`@RetrofitBuilder`中的配置将使用全局配置
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
这里的属性与`@RetrofitBuilder`中的属性完全相同，可以参考`@RetrofitBuilder`的注解说明

`overwrite-type` 提供了`global_first`和`local_first`两种模式，

当为`global_first`时，全局配置将会与`@RetrofitBuilder`配置合并,使用全局配置的属性，全局配置为空的属性将会使用`@RetrofitBuilder`配置中的属性

当为`local_first`时，全局配置将会与`@RetrofitBuilder`配置合并, 使用`@RetrofitBuilder`配置的属性，`@RetrofitBuilder`配置为空的将会使用全局配置的属性

在`@RetrofitBuilder`中提供`denyGlobalConfig = true`的配置，可以拒绝全局的配置，保持自己的独立性而不被全局配置污染
```java
@RetrofitBuilder(baseUrl = "http://localhost:8080/", denyGlobalConfig = true)
public interface HelloApi {
}
```
可以参考 [retrofit-spring-boot-starter-sample-global-config](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-global-config)

## 插件扩展功能
你可以为Retrofit编写基于Interceptor的扩展功能，

以@RetrofitCloudService 为例，这是一个基于Interceptor的扩展功能，可以实现Retrofit在Spring Cloud中的负载均衡

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
        final RetrofitApiServiceBean currentServiceBean = context.getRetrofitApiServiceBean(clazzName);
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
如上代码其实也就是下面移除章节的重构后成为组件的Spring Cloud负载均衡功能。

可以参考 [retrofit-spring-boot-starter-sample-plugin](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-starter-sample-plugin)


### ~~@RetrofitCloudService Annotation~~ (v0.0.20 移除)
此功能已从`retrofit-spring-boot-starter`移除，考虑的原因是`retrofit-spring-boot-starter`只作为SpringBoot或者说SpringFramework对Retrofit的封装和动态代理实现。

此功能属于一个附加的Spring Cloud功能，依赖了Spring Cloud相关的组件。在未来，将会以插件的方式提供扩展，并以`retrofit-spring-cloud-starter`的方式，重新支持

在 Spring Cloud 微服务集群, 你可以使用`@RetrofitCloudService`去调用其他方微服务的API。

这个功能依赖`spring-cloud-starter-loadbalancer`,将以下加入 pom.xml
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
`catalog`是其他微服务的服务名称，也就是在注册中心的名称

## 为什么这里会有另一个 retrofit-spring-boot-starter

首先感谢[lianjiatech](https://github.com/LianjiaTech/retrofit-spring-boot-starter)
提供一个近乎完美的项目[retrofit-spring-boot-starter](https://github.com/LianjiaTech/retrofit-spring-boot-starter).
但是,在使用中,我发现它会为每个API接口文件创建一个Retrofit实例,在我看来这是一个资源浪费.

看完代码,我觉得很难在短时间内修改原来的代码,所以我重复造了一个轮子.

在我的工作中，团队将使用Retrofit作为BFF层的HTTP客户端请求微服务,因此,有成百上千个API接口文件在BFF项目中.

最终我改进了创建Retrofit实例的时间以及按照RetrofitBuilder的属性合并了Retrofit一个实例的创建，并且允许一个Retrofit API接口继承一个基本接口，它可以定义和配置Retrofit属性.

## Maintainers

[@liuziyuan](https://github.com/liuziyuan)

## Contributing
Feel free to dive in! [Open an issue](https://github.com/liuziyuan/retrofit-spring-boot-starter/issues/new) or submit PRs.

Standard Readme follows the [Contributor Covenant](http://contributor-covenant.org/version/1/3/0/) Code of Conduct.

This project exists thanks to all the people who contribute.

## License

[MIT](LICENSE) © liuziyuan
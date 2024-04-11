# Retrofit Spring boot reactor starter
## 说明
简化Retrofit在Springboot项目中的使用，当你的项目中使用了`Spring WebFlux`组件，那么你可以使用`retrofit-spring-boot-reactor-starter`作为Retrofit Client进行Http请求,
组件提供了异步的Adapter和基于Jackson的Converter.


| jdk version | Springboot2 version    | Springboot3 version |
|-------------|------------------------|---------------------|
| jdk8        | 2.0.0.RELEASE -- 2.7.x | NA                  |
| jdk11       | 2.0.3.RELEASE -- 2.7.x | NA                  |
| jdk17       | 2.4.2 -- 2.7.x         | 3.0.0 - latest      |
| jdk21       | 2.7.17 -- 2.7.x        | 3.1.0 - latest      |


## 用法
1. 在pom.xml中添加依赖
```xml
<dependency>
    <groupId>io.github.liuziyuan</groupId>
    <artifactId>retrofit-spring-boot-reactor-starter</artifactId>
    <version>{latest version}</version>
</dependency>
```

2. 在Springboot启动项中启用Retrofit
```java
@EnableRetrofit({"xxx.yyy.zzz"})
@SpringBootApplication
public class RetrofitSpringBootWebApplication{}
```
`xxx.yyy.zzz`是Retrofit的接口包名，多个接口包名用逗号分隔。
当然，你也可以不指定。这样会全局扫描所有的类来加载Retrofit接口。

3. 在项目中创建Retrofit的接口,并使用`@RetrofitBuilder`注解指定Retrofit的baseUrl.

```java
import io.github.liuziyuan.retrofit.core.annotation.RetrofitBuilder;

@RetrofitBuilder(baseUrl = "http://localhost:8080")
public interface DemoApi {
    @GET("hello/{message}")
    Mono<HelloBean> hello(@Path("message") String message);

    @GET("hello/{message}")
    Observable<HelloBean> hello2(@Path("message") String message);
    
}
```
在`retrofit-spring-boot-reactor-starter`中，默认对Retrofit的Converter使用了`Jackson`，
对Retrofit的Adapter使用基于`Reactor`和`RxJava3`。

从上面的示例中可以看到，我们使用了`Mono<HelloBean>`, `Observable<HelloBean>`两种不同的返回类型。

`Mono<HelloBean>`和`Observable<HelloBean>`都是异步的返回类型。

`HelloBean`是一个简单的Bean类，他与被请求的接口方法的返回类型一致,这是一个来自于http://localhost:8080/hello/ 的请求代码结构。

下面是服务端的示例, 示例中定义了一个简单的Bean类`HelloBean` 和一个简单的Controller.
```java
@Data
public class HelloBean {
    private String message;
}

```

4. 在Controller中使用Retrofit接口进行请求
下面详细的展示了在Controller中使用Retrofit接口进行请求的多种方式
```java
@RestController
public class HelloController {

    @Autowired
    private HelloApi helloApi;

    @GetMapping("/v1/hello/{message}")
    public Mono<HelloBean> hello(@PathVariable String message) throws IOException {
        return helloApi.hello(message);
    }

    @GetMapping("/v2/hello/{message}")
    public Mono<HelloBean> hello2(@PathVariable String message) throws IOException {
        Observable<HelloBean> observable = helloApi.hello2(message);
        return Mono.from(observable.toFlowable(BackpressureStrategy.BUFFER));
    }

}
```

## 更多retrofit的自定义设置
`retrofit-spring-boot-rector-starter`是对 `retrofit-spring-boot-starter`的扩展，旨在简化Retrofit在Spring-webflux项目中的使用，所以你可以使用 `retrofit-spring-boot-starter`中的任何注解和配置来实现更复杂的自定义设置。
更多请参考 [retrofit-spring-boot-starter](https://github.com/liuziyuan/retrofit-spring-boot-starter/blob/main/README_CN.md) 的文档。

## 完整的代码示例
[Demo项目链接](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-reactor-starter-sample)



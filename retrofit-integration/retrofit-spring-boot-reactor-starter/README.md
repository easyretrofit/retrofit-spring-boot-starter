# Retrofit Spring boot reactor starter

[中文文档](https://github.com/liuziyuan/retrofit-spring-boot-reactor-starter/blob/master/README.zh_CN.md)

## Overview
Simplify the use of Retrofit in the Springboot project. When using the `Spring WebFlux` component in your project, you can use `retrofit-spring-boot-reactor-start` as the Retrofit client for HTTP requests,
The component provides a synchronized adapter and a converter implemented by `Jackson`

| jdk version | Springboot2 version    | Springboot3 version |
|-------------|------------------------|---------------------|
| jdk8        | 2.0.0.RELEASE -- 2.7.x | NA                  |
| jdk11       | 2.0.3.RELEASE -- 2.7.x | NA                  |
| jdk17       | 2.4.2 -- 2.7.x         | 3.0.0 - latest      |
| jdk21       | 2.7.17 -- 2.7.x        | 3.1.0 - latest      |



## Usage
1. Add dependencies in pom.xml
```xml
<dependency>
    <groupId>io.github.liuziyuan</groupId>
    <artifactId>retrofit-spring-boot-reactor-starter</artifactId>
    <version>{latest version}</version>
</dependency>
```

2. Enable Retrofit in the Springboot Boot option
```java
@EnableRetrofit({"xxx.yyy.zzz"})
@SpringBootApplication
public class RetrofitSpringBootWebApplication{}
```
`xxx.yyy.zzz` is the Retrofit interface package name. Multiple interface package names are separated by commas.
Of course, you don't have to specify. This scans all classes globally to load the Retrofit interface.

3. Create an interface to Retrofit in your project and specify the baseUrl for Retrofit using the `@RetrofitBuilder` annotation.

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
In `Retrofit-spring-boot-reactor-starter`, the Retrofit Converter uses Jackson by default.

The Adapter used for Retrofit is based on `Reactor` and `RxJava3`.

As you can see from the above example, we use `Mono<HelloBean>`, `Observable<HelloBean>` two different return types.

`Mono<HelloBean>` and `Observable<HelloBean>` are asynchronous return types.

`HelloBean` is a simple Bean class, he and the return type of the requested interface methods are consistent, this is a request from the http://localhost:8080/hello/ code structure.

Here is an example on the server side, where a simple Bean class `HelloBean` and a simple Controller are defined.
```java
@Data
public class HelloBean {
    private String message;
}

```

4. Requests are made in Controller using the Retrofit interface

The following shows in detail the various ways in which requests can be made in Controller using the Retrofit interface
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

## More customizations for retrofit
`retrofit-spring-boot-rector-starter`是对 `retrofit-spring-boot-starter`的扩展，旨在简化Retrofit在Spring-webflux项目中的使用，所以你可以使用 `retrofit-spring-boot-starter`中的任何注解和配置来实现更复杂的自定义设置。
More refer to [retrofit-spring-boot-starter](https://github.com/liuziyuan/retrofit-spring-boot-starter/blob/main/README_CN.md) 的文档。

## 完整的代码示例
[Demo Link](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-reactor-starter-sample)





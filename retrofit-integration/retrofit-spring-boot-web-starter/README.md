# Retrofit Spring boot web starter

[中文文档](https://github.com/liuziyuan/retrofit-spring-boot-web-starter/blob/master/README.zh_CN.md)

## Overview
Simplify the use of Retrofit in the Springboot project. When using the `Spring-Web` component in your project, you can use `retrofit-spring-boot-web-start` as the Retrofit client for HTTP requests,
The component provides a synchronized adapter and a converter implemented by `Jackson`

| jdk version | Springboot2 version    | Springboot3 version |
|-------------|------------------------|---------------------|
| jdk8        | 2.0.0.RELEASE -- 2.7.x | NA                  |
| jdk11       | 2.0.3.RELEASE -- 2.7.x | NA                  |
| jdk17       | 2.4.2 -- 2.7.x         | 3.0.0 - latest      |
| jdk21       | 2.7.16 -- 2.7.x        | 3.1.0 - latest      |


## 用法
1. Adding dependencies in pom.xml
```xml
<dependency>
    <groupId>io.github.liuziyuan</groupId>
    <artifactId>retrofit-spring-boot-web-starter</artifactId>
    <version>{latest version}</version>
</dependency>
```

2. Enable Retrofit in the Springboot startup
```java
@EnableRetrofit({"xxx.yyy.zzz"})
@SpringBootApplication
public class RetrofitSpringBootWebApplication{}
```
`xxx.yyy.zzz`是Retrofit的接口包名，多个接口包名用逗号分隔。
当然，你也可以不指定。这样会全局扫描所有的类来加载Retrofit接口。

3. Create an interface for Retrofit in the project and use the `@RetrofitBuilder` annotation to specify the baseUrl of Retrofit

```java
import io.github.liuziyuan.retrofit.core.annotation.RetrofitBuilder;

@RetrofitBuilder(baseUrl = "http://localhost:8080")
public interface DemoApi {
    @GET("hello/{message}")
    HelloBean hello(@Path("message") String message);

    @GET("hello/{message}")
    ListenableFuture<HelloBean> hello2(@Path("message") String message);

    @GET("hello/{message}")
    CompletableFuture<HelloBean> hello3(@Path("message") String message);
    
}
```
in `retrofit-spring-boot-web-starter`,By default, `Jackson` is used for Retrofit's Converter,
Using the `ListenableFuture` provided by Guava packages and the `Body` provided by customize for the Retrofit adapter`

From the above example, we can see that we used three different return types: `HelloBean`, `ListenableFuture<HelloBean>`, `CompletableFuture<HelloBean>`.

Among them, `HelloBean` is a synchronous return type that can be directly returned.

Both `ListenableFuture<HelloBean> `and `CompletableFuture<HelloBean>` are asynchronous return types that require calling `.get()` for thread blocking to obtain the return value.

`HelloBean` is a simple Bean class that matches the return type of the requested interface method, which is derived from http://localhost:8080/hello/ The request code structure for.

The following is an example of the server-side, which defines a simple Bean class `HelloBean` and a simple `Controller`
```java
@Data
public class HelloBean {
    private String message;
}

```

```java
@RestController
public class HelloController {

    @GetMapping("/hello/{message}")
    public ResponseEntity<HelloBean> hello(@PathVariable String message) {
        HelloBean helloBean = new HelloBean();
        helloBean.setMessage("Hello" + message);
        return ResponseEntity.ok(helloBean);
    }
}
```

4. Request using Retrofit interface in Controller

The following is a detailed demonstration of various ways to make requests using the Retrofit interface in the Controller
```java
@RestController
public class HelloController {

    @Autowired
    private HelloApi helloApi;

    @GetMapping("/v1/hello/{message}")
    public ResponseEntity<HelloBean> hello(@PathVariable String message) throws IOException {
        HelloBean hello = helloApi.hello(message);
        return ResponseEntity.ok(hello);
    }

    @GetMapping("/v2/hello/{message}")
    public ResponseEntity<HelloBean> hello2(@PathVariable String message) throws IOException, ExecutionException, InterruptedException {
        ListenableFuture<HelloBean> helloBeanListenableFuture = helloApi.hello2(message);
        HelloBean helloBean = helloBeanListenableFuture.get();
        return ResponseEntity.ok(helloBean);
    }

    @GetMapping("/v3/hello/{message}")
    public ResponseEntity<HelloBean> hello3(@PathVariable String message) throws IOException, ExecutionException, InterruptedException {
        CompletableFuture<HelloBean> completableFuture = helloApi.hello3(message);
        HelloBean helloBean = completableFuture.get();
        return ResponseEntity.ok(helloBean);
    }
}
```

## More custom settings for retrofit
`retrofit-spring-boot-web-starter` is an extension to `retrofit-spring-boot-starter`, aimed at simplifying the use of Retrofit in `Spring-web` projects, 
so you can use any annotations and configurations in `retrofit-spring-boot-starter` to achieve more complex custom settings.
For more information, please refer to the [retrofit-spring-boot-starter](https://github.com/liuziyuan/retrofit-spring-boot-starter/blob/main/README_CN.md) documentation

## Demo
[Demo Project Link](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-web-starter-sample)



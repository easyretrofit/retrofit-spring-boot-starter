# Retrofit Spring boot web starter
## 说明
简化Retrofit在Springboot项目中的使用，当你的项目中使用了Spring Web组件，那么你可以使用`retrofit-spring-boot-web-starter`作为Retrofit Client进行Http请求,
组件提供了同步的Adapter和给予Jackson的Converter.


| jdk version | Springboot2 version    | Springboot3 version |
|-------------|------------------------|---------------------|
| jdk8        | 2.0.0.RELEASE -- 2.7.x | NA                  |
| jdk11       | 2.0.3.RELEASE -- 2.7.x | NA                  |
| jdk17       | 2.4.2 -- 2.7.x         | 3.0.0 - latest      |
| jdk21       | 2.7.16 -- 2.7.x        | 3.1.0 - latest      |


## 用法
1. 在pom.xml中添加依赖
```xml
<dependency>
    <groupId>io.github.liuziyuan</groupId>
    <artifactId>retrofit-spring-boot-web-starter</artifactId>
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
    HelloBean hello(@Path("message") String message);

    @GET("hello/{message}")
    ListenableFuture<HelloBean> hello2(@Path("message") String message);

    @GET("hello/{message}")
    CompletableFuture<HelloBean> hello3(@Path("message") String message);
    
}
```
在`retrofit-spring-boot-web-starter`中，默认对Retrofit的Converter使用了`Jackson`，
对Retrofit的Adapter使用基于`Body`和Guava包提供的`ListenableFuture`。

从上面的示例中可以看到，我们使用了`HelloBean`, `ListenableFuture<HelloBean>`, `CompletableFuture<HelloBean>`三种不同的返回类型。

其中`HelloBean`是同步的返回类型，可以直接返回。

其中`ListenableFuture<HelloBean>`和`CompletableFuture<HelloBean>`都是异步的返回类型，需要调用.get()进行线程阻塞来获取返回值。

`HelloBean`是一个简单的Bean类，他与被请求的接口方法的返回类型一致,这是一个来自于http://localhost:8080/hello/ 的请求代码结构。

下面是服务端的示例, 示例中定义了一个简单的Bean类`HelloBean` 和一个简单的Controller.
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

4. 在Controller中使用Retrofit接口进行请求
下面详细的展示了在Controller中使用Retrofit接口进行请求的多种方式
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

## 更多retrofit的自定义设置
`retrofit-spring-boot-web-starter`是对 `retrofit-spring-boot-starter`的扩展，旨在简化Retrofit在Spring-web项目中的使用，所以你可以使用 `retrofit-spring-boot-starter`中的任何注解和配置来实现更复杂的自定义设置。
更多请参考 [retrofit-spring-boot-starter](https://github.com/liuziyuan/retrofit-spring-boot-starter/blob/main/README_CN.md) 的文档。

## 完整的代码示例
[Demo项目链接](https://github.com/liuziyuan/retrofit-spring-boot-starter-samples/tree/main/retrofit-spring-boot-web-starter-sample)



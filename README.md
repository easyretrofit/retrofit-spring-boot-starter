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

```
<dependency>
   <groupId>io.github.liuziyuan</groupId>
   <artifactId>retrofit-spring-boot-starter</artifactId>
   <version>0.0.3</version>
</dependency>
```

### Add `@EnableRetrofit` to your Spring boot starter Class, Create a config class for Retrofit

```
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

```
@Configuration
public class RetrofitConfig {
    @Bean
    public RetrofitResourceDefinitionRegistry retrofitResourceDefinitionRegistry() {
        return new RetrofitResourceDefinitionRegistry();
    }
}

```

### Create an Interface file, and use `@RetrofitBuilder`

 ```
@RetrofitBuilder(baseUrl = "${app.test.base-url}")  
public interface TestApi {  
  
    @GET("/v1/test/")  
    Call<Result> test();  
}
```

pls keep app.test.base-url on your resources' config file, baseUrl can also be a URL as http://localhost:8080

### Use Retrofit API On Controller

```
@Slf4j  
@RestController  
@RequestMapping("/v1/hello")  
public class HelloController {  
  @Autowired  
  private TestApi api;
  ```

### Yes, Congratulations, your code should work normally.

## Advanced usage

## Add other attributes for  `@RetrofitBuilder`, if you need


You can set the other properties of Retrofit in @RetrofitBuilder
```
@RetrofitBuilder(baseUrl = "${app.test.base-url}",  
  addConverterFactory = {GsonConverterFactory.class, JacksonConverterFactory.class},  
  addCallAdapterFactory = {RxJavaCallAdapterFactory.class},  
  client = MyOkHttpClient.class)  
@RetrofitInterceptor(handler = MyRetrofitInterceptor1.class)  
@RetrofitInterceptor(handler = MyRetrofitInterceptor2.class)  
public interface TestApi {  
  
    @GET("/v1/test/")  
    Call<Result> test();  
}
```

### Set your custom OKHttpClient

your customize OKHttpClient need extends BaseOkHttpClientBuilder

```
@Component
public class MyOkHttpClient extends BaseOkHttpClientBuilder {  
  
  @Override  
  public OkHttpClient.Builder builder(OkHttpClient.Builder builder) {  
        return builder;  
  }  
}
```

and set `MyOkHttpClient` to `client = MyOkHttpClient.class` in @RetrofitBuilder

### Set your custom OKHttpClient Interceptor

you could add your customize OKHttpClient Interceptor, need extends BaseInterceptor

```
@Component
public class MyRetrofitInterceptor2 extends BaseInterceptor {  
  
 @SneakyThrows  
 @Override  protected Response executeIntercept(Chain chain) {  
        Request request = chain.request();  
        return chain.proceed(request);  
  }  
}
```

and set class name like `@RetrofitInterceptor(handler = MyRetrofitInterceptor1.class)  ` 

#### When you only want to set the interceptor without making other modifications to the OKHttpClient object, you can delete the client property of @RetrofitBuilder and There is no need to customize your OKHttpClient

### Set include,exclude and sort for @RetrofitInterceptor
you could set include,exclude and sort properties in @RetrofitInterceptor
like `@RetrofitInterceptor(handler = MyRetrofitInterceptor2.class, exclude = {"/v1/test/"})`

When exclude is used, the corresponding API will ignore this interceptor.

When you use sort, please ensure that all interceptors use sort, because by default, sort is 0. You can ensure the
execution order of your interceptors through int type. By default, the interceptor is loaded from top to bottom.

### Interface inheritance
If you have hundreds of Interface method, it is from a same source Base URL, and you want your code structure to be
   more orderly and look consistent with the source service structure, you could do this,

#### Define an empty Interface file

 ```
@RetrofitBuilder(baseUrl = "${app.test.base-url}",  
  addConverterFactory = {GsonConverterFactory.class, JacksonConverterFactory.class},  
  addCallAdapterFactory = {RxJavaCallAdapterFactory.class},  
  client = MyOkHttpClient.class)  
@RetrofitInterceptor(handler = MyRetrofitInterceptor1.class)  
@RetrofitInterceptor(handler = MyRetrofitInterceptor2.class)  
public interface TestApi {  

}
```

and create other API Interface extend Parent class

```
public interface TestInheritApi extends TestApi {  
  
    @GET("/v1/test/inherit/")  
    Call<Result> test1();  
}
```

Please try not to use the parent class in the injected place


#### If you inject the parent Interface and the inherited Interface at the same place, the following errors may occur

```
Description:

Field api in io.liuziyuan.demo.controller.HelloController required a single bean, but 2 were found:
	- io.liuziyuan.demo.api.TestApi: defined in null
	- io.liuziyuan.demo.api.TestInheritApi: defined in null

Action:

Consider marking one of the beans as @Primary, updating the consumer to accept multiple beans, or using @Qualifier to identify the bean that should be consumed
```

So, you need use @Qualifier("io.liuziyuan.demo.api.TestApi")


```
@Slf4j  
@RestController  
@RequestMapping("/v1/hello")  
public class HelloController {  
  @Autowired  
  @Qualifier("io.liuziyuan.demo.api.TestApi")  
  private TestApi api;
  @Autowired  
  @Qualifier("io.liuziyuan.demo.api.TestInheritApi")  
  private TestInheritApi api2;
  ```



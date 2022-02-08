# retrofit-spring-boot-starter
## Why is there another retrofit-spring-boot-starter

First of all, thank [lianjiatech](https://github.com/LianjiaTech/retrofit-spring-boot-starter) for providing an almost perfect project of retrofit-spring-boot-starter.

However, in use, I found that it will create a retrofit instance for each API Interface file, which in my opinion is a waste of resources. After reading the code, I think it is difficult to modify the original basis in a short time, so I repeated a wheel.

In my work, the team will use retrofit as the API of BFF layer HTTP client to request micro services. Therefore, there will be hundreds of interface files in BFF. Therefore, I improved the time of creating retrofit instance, allowing one retrofit interface to inherit one base interface, which can define and configure retrofit attributes

You can see the effect I want from the fourth step of introduction

## How to use it
1. add `@EnableRetrofit` to your Spring boot Starter Class
```
@EnableRetrofit  
@Slf4j  
public class HelloApplication extends SpringBootServletInitializer {  
    public static void main(String[] args) {  
        SpringApplication.run(HelloApplication.class, args);  
  }  
}
```
2. create an Interface file, and use `@RetrofitBuilder`
 ```
@RetrofitBuilder(baseUrl = "${app.test.base-url}")  
public interface TestApi {  
  
    @GET("/v1/test/")  
    Call<Result> test();  
}
```
pls keep app.test.base-url on your resources config file
baseUrl can also be a URL as http://xxx or https://xxx

3. add other attributes for  `@RetrofitBuilder`
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
your  custom OKHttpClient need extends BaseOkHttpClientBuilder
```
public class MyOkHttpClient extends BaseOkHttpClientBuilder {  
  
    @Override  
  public OkHttpClient.Builder builder(OkHttpClient.Builder builder) {  
        return builder;  
  }  
}
```
and you could add your custom OKHttpClient  Interceptor
```
public class MyRetrofitInterceptor2 extends BaseInterceptor {  
    public MyRetrofitInterceptor2(RetrofitResourceContext context) {  
        super(context);  
  }  
  
    @SneakyThrows  
 @Override  protected Response executeIntercept(Chain chain) {  
        Request request = chain.request();  
 return chain.proceed(request);  
  }  
}
```

4. If you have hundreds of Interface method, it is from a source  Base URL ,  and you want your code structure to be more orderly and look consistent with the source service structure，you could do this ,
   Define an empty Interface file
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
5. Use Retrofit API On Controller
```
@Slf4j  
@RestController  
@RequestMapping("/v1/hello")  
public class HelloController {  
  @Autowired  
//    @Qualifier("com.liuziyuan.demo.api.TestApi2")  
  private TestApi2 api2;
  ```

If you inject the Interface and the inherited Interface at the same time, the following errors may occur

```
Description:

Field api in com.liuziyuan.demo.controller.HelloController required a single bean, but 2 were found:
	- com.liuziyuan.demo.api.TestApi: defined in null
	- com.liuziyuan.demo.api.TestInheritApi: defined in null


Action:

Consider marking one of the beans as @Primary, updating the consumer to accept multiple beans, or using @Qualifier to identify the bean that should be consumed
```
So, you need use @Qualifier("com.liuziyuan.demo.api.TestApi")

Each API interface in the project has been set with the qualifier attribute, so you do not do anything.


package io.github.liuziyuan.retrofit.exception;

/**
 * @author liuziyuan
 */
public class Message {

    public static final String WARNING_RETROFIT_CLIENT_EMPTY = "The [Retrofit Client] is not found in the 'RetrofitResourceContext'. You may not be using @RetrofitBuilder in the basePackages";
    public static final String RETROFIT_RESOURCE_CONTEXT_NOT_FOUND = "The 'RetrofitResourceContext' object not found in the Spring 'ApplicationContext'. You may not be using @EnableRetrofit";
}

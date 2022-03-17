package io.github.liuziyuan.retrofit.annotation;

import io.github.liuziyuan.retrofit.extension.*;
import okhttp3.HttpUrl;
import retrofit2.http.GET;

import java.lang.annotation.*;

/**
 * Annotation of Retrofit.Builder
 *
 * @author liuziyuan
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RetrofitBuilder {
    /**
     * Set the API base URL.
     *
     * <p>The specified endpoint values (such as with {@link GET @GET}) are resolved against this
     * value using {@link HttpUrl#resolve(String)}. The behavior of this matches that of an {@code
     * <a href="">} link on a website resolving on the current URL.
     *
     * <p><b>Base URLs should always end in {@code /}.</b>
     *
     * <p>A trailing {@code /} ensures that endpoints values which are relative paths will correctly
     * append themselves to a base which has path components.
     *
     * <p><b>Correct:</b><br>
     * Base URL: http://example.com/api/<br>
     * Endpoint: foo/bar/<br>
     * Result: http://example.com/api/foo/bar/
     *
     * <p><b>Incorrect:</b><br>
     * Base URL: http://example.com/api<br>
     * Endpoint: foo/bar/<br>
     * Result: http://example.com/foo/bar/
     *
     * <p>This method enforces that {@code baseUrl} has a trailing {@code /}.
     *
     * <p><b>Endpoint values which contain a leading {@code /} are absolute.</b>
     *
     * <p>Absolute values retain only the host from {@code baseUrl} and ignore any specified path
     * components.
     *
     * <p>Base URL: http://example.com/api/<br>
     * Endpoint: /foo/bar/<br>
     * Result: http://example.com/foo/bar/
     *
     * <p>Base URL: http://example.com/<br>
     * Endpoint: /foo/bar/<br>
     * Result: http://example.com/foo/bar/
     *
     * <p><b>Endpoint values may be a full URL.</b>
     *
     * <p>Values which have a host replace the host of {@code baseUrl} and values also with a scheme
     * replace the scheme of {@code baseUrl}.
     *
     * <p>Base URL: http://example.com/<br>
     * Endpoint: https://github.com/square/retrofit/<br>
     * Result: https://github.com/square/retrofit/
     *
     * <p>Base URL: http://example.com<br>
     * Endpoint: //github.com/square/retrofit/<br>
     * Result: http://github.com/square/retrofit/ (note the scheme stays 'http')
     *
     * @return String URL
     */
    String baseUrl() default "";

    Class<? extends BaseCallAdapterFactoryBuilder>[] addCallAdapterFactory() default {};

    Class<? extends BaseConverterFactoryBuilder>[] addConverterFactory() default {};

    Class<? extends BaseOkHttpClientBuilder> client() default BaseOkHttpClientBuilder.class;

    Class<? extends BaseCallBackExecutorBuilder> callbackExecutor() default BaseCallBackExecutorBuilder.class;

    Class<? extends BaseCallFactoryBuilder> callFactory() default BaseCallFactoryBuilder.class;

    boolean validateEagerly() default false;
}

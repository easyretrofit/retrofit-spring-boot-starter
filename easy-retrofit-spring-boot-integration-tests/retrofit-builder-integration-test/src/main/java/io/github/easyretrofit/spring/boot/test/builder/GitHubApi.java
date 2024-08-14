package io.github.easyretrofit.spring.boot.test.builder;

import com.google.common.util.concurrent.ListenableFuture;
import io.github.easyretrofit.core.annotation.RetrofitBuilder;
import io.github.easyretrofit.spring.boot.test.builder.common.GsonConverterFactoryBuilder;
import io.github.easyretrofit.spring.boot.test.builder.common.JacksonConverterFactoryBuilder;
import io.github.easyretrofit.spring.boot.test.builder.common.MyOkHttpClientBuilder;
import io.github.easyretrofit.spring.boot.test.builder.common.GuavaCallAdapterFactoryBuilder;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RetrofitBuilder(baseUrl = "${github.base-url}",
        addConverterFactory = {GsonConverterFactoryBuilder.class, JacksonConverterFactoryBuilder.class},
        addCallAdapterFactory = {GuavaCallAdapterFactoryBuilder.class},
        client = MyOkHttpClientBuilder.class
)
public interface GitHubApi {

    @GET("/repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> contributors(@Path("owner") String owner, @Path("repo") String repo);

    @GET("/repos/{owner}/{repo}/contributors")
    CompletableFuture<List<Contributor>> contributorsAsync(@Path("owner") String owner, @Path("repo") String repo);

    @GET("/repos/{owner}/{repo}/contributors")
    ListenableFuture<List<Contributor>> listenableFuture(@Path("owner") String owner, @Path("repo") String repo);
}

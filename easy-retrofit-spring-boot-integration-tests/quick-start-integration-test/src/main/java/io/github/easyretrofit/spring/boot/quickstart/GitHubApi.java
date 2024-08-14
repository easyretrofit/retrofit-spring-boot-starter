package io.github.easyretrofit.spring.boot.quickstart;

import io.github.easyretrofit.core.annotation.RetrofitBuilder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

@RetrofitBuilder(baseUrl = "${github.base-url}")
public interface GitHubApi {

    @GET("/repos/{owner}/{repo}/contributors")
    Call<ResponseBody> contributors(@Path("owner") String owner, @Path("repo") String repo);
}

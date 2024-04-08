package io.github.liuziyuan.retrofit.adapter.reactor;

import okhttp3.HttpUrl;
import retrofit2.Call;

import java.net.URI;

public class CallReCreatingFactory {


    public static <T> Call<T> create(Call<T> originalCall, URI uri) {
        Call<T> call = originalCall.clone();
        if (uri == null) {
            return call;
        }
        HttpUrl httpUrl = HttpUrl.get(uri);
        HttpUrl newUrl = httpUrl.newBuilder()
                .scheme(httpUrl.scheme())
                .host(httpUrl.host())
                .port(httpUrl.port())
                .build();
        call.request().newBuilder().url(newUrl).build();
        return call;
    }
}

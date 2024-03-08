package io.github.liuziyuan.test.retrofit.spring.boot.inherit;

import io.github.liuziyuan.retrofit.core.annotation.RetrofitBuilder;

@RetrofitBuilder(denyGlobalConfig = true, baseUrl = "http://localhost:9000")
public interface OtherApi {
}

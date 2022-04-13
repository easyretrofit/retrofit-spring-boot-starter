package io.github.liuziyuan.retrofit.demo.api;

import io.github.liuziyuan.retrofit.annotation.RetrofitBuilder;
import io.github.liuziyuan.retrofit.annotation.RetrofitDynamicBaseUrl;

/**
 * @author liuziyuan
 */
@RetrofitBuilder
@RetrofitDynamicBaseUrl("http://localhost:9990")
public interface DummyHostUrlApi1 {
}

package io.github.liuziyuan.retrofit.demo.api;

import io.github.liuziyuan.retrofit.core.annotation.RetrofitBuilder;
import io.github.liuziyuan.retrofit.core.annotation.RetrofitDynamicBaseUrl;

/**
 * @author liuziyuan
 */
@RetrofitBuilder
@RetrofitDynamicBaseUrl("http://localhost:9990")
public interface DummyHostUrlApi1 {
}

package io.github.liuziyuan.retrofit.demo.api;

import io.github.liuziyuan.retrofit.core.annotation.RetrofitDynamicBaseUrl;

/**
 * @author liuziyuan
 */
@RetrofitDynamicBaseUrl("http://localhost:9991")
public interface DummyHostUrlApi2 extends DummyHostUrlApi1 {
}

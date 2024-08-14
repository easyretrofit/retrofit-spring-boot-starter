package io.github.liuziyuan.test.retrofit.spring.boot.inherit;

import io.github.easyretrofit.core.annotation.RetrofitBase;

@RetrofitBase(baseInterface = BaseApi.class)
public interface MixedApi extends OtherApi{
}

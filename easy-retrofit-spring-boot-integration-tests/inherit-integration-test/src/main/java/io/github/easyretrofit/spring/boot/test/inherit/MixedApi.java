package io.github.easyretrofit.spring.boot.test.inherit;

import io.github.easyretrofit.core.annotation.RetrofitBase;

@RetrofitBase(baseInterface = BaseApi.class)
public interface MixedApi extends OtherApi{
}

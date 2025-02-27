package io.github.easyretrofit.spring.boot.it.inherit;

import io.github.easyretrofit.core.annotation.RetrofitBase;
import io.github.easyretrofit.core.annotation.RetrofitFallBack;

@RetrofitFallBack(G1L3ApiApiFallBack.class)
@RetrofitBase(baseInterface = G1L2Api.class)
public interface G1L3Api {
}

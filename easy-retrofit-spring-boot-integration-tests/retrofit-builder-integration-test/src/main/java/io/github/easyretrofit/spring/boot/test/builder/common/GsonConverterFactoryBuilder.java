package io.github.easyretrofit.spring.boot.test.builder.common;

import io.github.easyretrofit.core.builder.BaseConverterFactoryBuilder;
import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author liuziyuan
 */
public class GsonConverterFactoryBuilder extends BaseConverterFactoryBuilder {
    @Override
    public Converter.Factory buildConverterFactory() {
        return GsonConverterFactory.create();
    }
}

package io.github.easyretrofit.spring.boot.test.builder.common;

import io.github.easyretrofit.core.builder.BaseConverterFactoryBuilder;
import retrofit2.Converter;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * @author liuziyuan
 */
public class JacksonConverterFactoryBuilder extends BaseConverterFactoryBuilder {
    @Override
    public Converter.Factory buildConverterFactory() {
        return JacksonConverterFactory.create();
    }
}

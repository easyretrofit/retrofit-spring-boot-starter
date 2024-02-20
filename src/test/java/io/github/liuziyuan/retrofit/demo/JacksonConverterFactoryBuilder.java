package io.github.liuziyuan.retrofit.demo;

import io.github.liuziyuan.retrofit.core.extension.BaseConverterFactoryBuilder;
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

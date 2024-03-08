package io.github.liuziyuan.test.retrofit.spring.boot.inject.builder;

import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.github.liuziyuan.retrofit.core.builder.BaseConverterFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import retrofit2.Converter;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * @author liuziyuan
 */
@Component
public class JacksonConverterFactoryBuilder extends BaseConverterFactoryBuilder {
    @Autowired
    private RetrofitResourceContext retrofitResourceContext;
    @Autowired
    private ApplicationContext context;
    @Override
    public Converter.Factory buildConverterFactory() {
        return JacksonConverterFactory.create();
    }
}

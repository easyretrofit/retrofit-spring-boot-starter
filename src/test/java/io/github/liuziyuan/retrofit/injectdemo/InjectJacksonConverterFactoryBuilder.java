package io.github.liuziyuan.retrofit.injectdemo;

import io.github.liuziyuan.retrofit.core.extension.BaseConverterFactoryBuilder;
import org.springframework.stereotype.Component;
import retrofit2.Converter;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * @author liuziyuan
 */
@Component
public class InjectJacksonConverterFactoryBuilder extends BaseConverterFactoryBuilder {
    @Override
    public Converter.Factory buildConverterFactory() {
        return JacksonConverterFactory.create();
    }
}

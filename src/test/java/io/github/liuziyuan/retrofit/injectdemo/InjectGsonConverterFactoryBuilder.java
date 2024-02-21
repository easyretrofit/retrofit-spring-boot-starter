package io.github.liuziyuan.retrofit.injectdemo;

import io.github.liuziyuan.retrofit.core.builder.BaseConverterFactoryBuilder;
import org.springframework.stereotype.Component;
import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author liuziyuan
 */
@Component
public class InjectGsonConverterFactoryBuilder extends BaseConverterFactoryBuilder {
    @Override
    public Converter.Factory buildConverterFactory() {
        return GsonConverterFactory.create();
    }
}

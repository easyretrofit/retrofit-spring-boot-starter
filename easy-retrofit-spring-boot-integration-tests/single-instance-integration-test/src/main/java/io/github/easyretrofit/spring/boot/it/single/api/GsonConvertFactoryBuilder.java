package io.github.easyretrofit.spring.boot.it.single.api;


import io.github.easyretrofit.core.builder.BaseConverterFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author liuziyuan
 */
@Slf4j
@Component
public class GsonConvertFactoryBuilder extends BaseConverterFactoryBuilder {

    @Value("${okhttpclient.timeout}")
    private int timeout;
    @Override
    public Converter.Factory buildConverterFactory() {
        log.info("time out: {}", timeout);
        return GsonConverterFactory.create();
    }
}

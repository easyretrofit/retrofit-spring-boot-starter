package io.github.liuziyuan.retrofit.extension;

import retrofit2.Converter;

/**
 * The Builder of Retrofit Converter.Factory
 * @author liuziyuan
 */
public abstract class BaseConverterFactoryBuilder extends BaseBuilder<Converter.Factory> {

    /**
     * build retrofit Converter.Factory
     *
     * @return Converter.Factory
     */
    public abstract Converter.Factory buildConverterFactory();

    @Override
    public Converter.Factory executeBuild() {
        return buildConverterFactory();
    }
}

package io.github.liuziyuan.retrofit.extension;

import okhttp3.Call;

/**
 * The Builder of Retrofit Call.Factory
 * @author liuziyuan
 */
public abstract class BaseCallFactoryBuilder extends BaseBuilder<Call.Factory> {

    /**
     * build OKHttp Call.Factory
     *
     * @return
     */
    public abstract Call.Factory buildCallFactory();

    @Override
    public Call.Factory executeBuild() {
        return buildCallFactory();
    }
}

package io.github.liuziyuan.retrofit.extension;

import retrofit2.CallAdapter;

/**
 * @author liuziyuan
 */
public abstract class BaseCallAdapterFactoryBuilder extends BaseBuilder<CallAdapter.Factory> {

    /**
     * build retrofit CallAdapter.Factory
     *
     * @return Executor
     */
    public abstract CallAdapter.Factory buildCallAdapterFactory();

    @Override
    public CallAdapter.Factory executeBuild() {
        return buildCallAdapterFactory();
    }
}

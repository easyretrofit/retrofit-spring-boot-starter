package io.github.liuziyuan.retrofit.extension;

import java.util.concurrent.Executor;

/**
 * @author liuziyuan
 */
public abstract class BaseCallBackExecutorBuilder extends BaseBuilder<Executor> {

    /**
     * build retrofit callBackExecutor
     * @return Executor
     */
    public abstract Executor buildCallBackExecutor();

    @Override
    public Executor executeBuild() {
        return buildCallBackExecutor();
    }
}

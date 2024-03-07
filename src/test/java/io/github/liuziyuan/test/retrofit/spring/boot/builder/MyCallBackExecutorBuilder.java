package io.github.liuziyuan.test.retrofit.spring.boot.builder;

import io.github.liuziyuan.retrofit.core.builder.BaseCallBackExecutorBuilder;

import java.util.concurrent.Executor;

/**
 * @author liuziyuan
 */
public class MyCallBackExecutorBuilder extends BaseCallBackExecutorBuilder {

    @Override
    public Executor buildCallBackExecutor() {
        return command -> command.run();
    }
}

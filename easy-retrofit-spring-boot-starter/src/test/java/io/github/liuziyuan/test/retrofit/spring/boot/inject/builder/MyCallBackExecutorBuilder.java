package io.github.liuziyuan.test.retrofit.spring.boot.inject.builder;

import io.github.easyretrofit.core.RetrofitResourceContext;
import io.github.easyretrofit.core.builder.BaseCallBackExecutorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

/**
 * @author liuziyuan
 */
@Component
public class MyCallBackExecutorBuilder extends BaseCallBackExecutorBuilder {
    @Autowired
    private RetrofitResourceContext retrofitResourceContext;
    @Autowired
    private ApplicationContext context;
    @Override
    public Executor buildCallBackExecutor() {
        return command -> command.run();
    }
}

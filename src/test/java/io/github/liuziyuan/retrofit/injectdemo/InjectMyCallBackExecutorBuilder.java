package io.github.liuziyuan.retrofit.injectdemo;

import io.github.liuziyuan.retrofit.extension.BaseCallBackExecutorBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

/**
 * @author liuziyuan
 */
@Component
public class InjectMyCallBackExecutorBuilder extends BaseCallBackExecutorBuilder {

    @Override
    public Executor buildCallBackExecutor() {
        return command -> command.run();
    }
}

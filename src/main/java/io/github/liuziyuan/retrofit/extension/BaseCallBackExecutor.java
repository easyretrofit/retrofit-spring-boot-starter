package io.github.liuziyuan.retrofit.extension;

import java.util.concurrent.Executor;

/**
 * @author liuziyuan
 */
public abstract class BaseCallBackExecutor implements Executor {

    /**
     * Executor execute method of retrofit callbackExecutor()
     * @param command Runnable
     */
    public abstract void executeCallBack(Runnable command);

    @Override
    public void execute(Runnable command) {
        executeCallBack(command);
    }
}

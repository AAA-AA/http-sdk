package github.com.http.sdk.proxy;

import github.com.http.sdk.handler.ParamSerializer;
import github.com.http.sdk.handler.ResultHandler;

/**
 * @author : hongqiangren.
 * @since: 2018/6/12 23:54
 */
public interface Execution<T> {

    T execute(Invocation invocation) throws Throwable;

    Invoker<?> getInvoker();

    String getServer();

    ResultHandler getResultHandler();

    ParamSerializer getHttpSerializer();
}

package com.github.http.sdk.proxy;


import com.github.http.sdk.handler.ParamSerializer;
import com.github.http.sdk.handler.ResultHandler;

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

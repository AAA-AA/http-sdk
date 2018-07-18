package com.github.http.sdk.filter;

import com.github.http.sdk.proxy.Invocation;
import com.github.http.sdk.proxy.Invoker;

/**
 * @author : hongqiangren.
 * @since: 2018/6/13 00:06
 */
public interface Filter {
    Object invoke(Invoker<?> invoker, Invocation invocation) throws Throwable;
}

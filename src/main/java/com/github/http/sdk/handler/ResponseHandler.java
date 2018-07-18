package com.github.http.sdk.handler;


import com.github.http.sdk.proxy.Invocation;

import java.io.IOException;

/**
 * @author : hongqiangren.
 * @since: 2018/6/12 22:54
 */
public interface ResponseHandler<T, R> {

    T handle(Invocation invocation, R response) throws IOException;
}

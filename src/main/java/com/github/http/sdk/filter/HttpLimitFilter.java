package com.github.http.sdk.filter;

import com.github.http.sdk.anno.Http;
import com.github.http.sdk.proxy.Invocation;
import com.github.http.sdk.proxy.Invoker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * @author : hongqiangren.
 * @since: 2018/6/13 00:09
 */
public class HttpLimitFilter implements Filter {

    private static final Map<Http, Semaphore> SEMAPHORES = new ConcurrentHashMap<>();

    @Override
    public Object invoke(Invoker<?> invoker, Invocation invocation) throws Throwable {
        Http http = invocation.annotationOf(Http.class, Invocation.METHOD);
        if (http.limit() > 100) {
            return invoker.invoke(invocation);
        }
        SEMAPHORES.putIfAbsent(http, new Semaphore(http.limit()));
        try {
            SEMAPHORES.get(http).acquire();
            return invoker.invoke(invocation);
        } finally {
            SEMAPHORES.get(http).release();
        }
    }
}

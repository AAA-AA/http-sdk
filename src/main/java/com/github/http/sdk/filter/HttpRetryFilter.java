package com.github.http.sdk.filter;

import com.github.http.sdk.anno.Http;
import com.github.http.sdk.proxy.Invocation;
import com.github.http.sdk.proxy.Invoker;
import com.github.http.sdk.utils.Clean;
import github.com.http.sdk.anno.Http;
import github.com.http.sdk.proxy.Invocation;
import github.com.http.sdk.proxy.Invoker;
import github.com.http.sdk.utils.Clean;

/**
 * @author : hongqiangren.
 * @since: 2018/6/13 00:15
 */
public class HttpRetryFilter implements Filter {
    @Override
    public Object invoke(Invoker<?> invoker, Invocation invocation) throws Throwable {
        Http http = invocation.annotationOf(Http.class, Invocation.METHOD);
        int retries = http.retries();
        for (int i = 0; i < retries - 1; i++) {
            try {
                return invoker.invoke(invocation);
            } catch (Exception e) {
                if (!canRetry(e)) {
                    throw e;
                }
                if (shouldDelay(e) && http.delay() < 20000) {
                    Thread.sleep(http.delay());
                }
            }
        }
        return invoker.invoke(invocation);
    }

    public boolean shouldDelay(Throwable e) {
        boolean delay = instanceOf(e, java.net.SocketException.class);
        delay = delay || instanceOf(e, java.net.SocketTimeoutException.class);
        delay = delay || instanceOf(e, org.apache.http.conn.ConnectTimeoutException.class);
        delay = delay || instanceOf(e, org.apache.http.conn.ConnectionPoolTimeoutException.class);
        return delay;
    }

    public boolean canRetry(Throwable e) {
        boolean can = instanceOf(e, java.net.SocketTimeoutException.class);
        can = can || instanceOf(e, java.net.SocketException.class);
        can = can || instanceOf(e, java.net.ConnectException.class);
        can = can || instanceOf(e, org.apache.http.conn.ConnectTimeoutException.class);
        can = can || instanceOf(e, org.apache.http.NoHttpResponseException.class);
        can = can || instanceOf(e, java.sql.SQLException.class);
        can = can || instanceOf(e, org.apache.http.conn.ConnectionPoolTimeoutException.class);
        return can;
    }

    private boolean instanceOf(Throwable e, Class<? extends Throwable> ex) {
        return Clean.indexOfThrowable(e, ex) > -1;
    }
}

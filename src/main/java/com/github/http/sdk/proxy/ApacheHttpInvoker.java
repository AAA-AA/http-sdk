package com.github.http.sdk.proxy;

import com.github.http.sdk.core.HttpClient;
import github.com.http.sdk.core.HttpClient;

/**
 * @author : hongqiangren.
 * @since: 2018/6/12 22:45
 */
public class ApacheHttpInvoker implements Invoker<byte[]>{
    @Override
    public byte[] invoke(Invocation invocation) throws Throwable {
        return HttpClient.single.execute(invocation);
    }
}

package service;

import github.com.http.sdk.filter.Filter;
import github.com.http.sdk.proxy.Invocation;
import github.com.http.sdk.proxy.Invoker;

/**
 * @author : hongqiangren.
 * @since: 2018/6/14 16:22
 */
public class AccessFilter implements Filter {

    @Override
    public Object invoke(Invoker<?> invoker, Invocation invocation) throws Throwable {
        String token = "token";
        Long brandId = invocation.getTransient("brandId");
        invocation.addQuery("access_token",token);
        return invoker.invoke(invocation);
    }
}

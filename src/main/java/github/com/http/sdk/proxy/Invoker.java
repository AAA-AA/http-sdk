package github.com.http.sdk.proxy;

/**
 * @author : hongqiangren.
 * @since: 2018/6/12 22:39
 */
public interface Invoker<T> {

    T invoke(Invocation invocation) throws Throwable;
}

package github.com.http.sdk.function;

/**
 * @author : hongqiangren.
 * @since: 2018/6/12 23:07
 */
@FunctionalInterface
public interface TExecutor<T> {
    T execute() throws Throwable;
}

package github.com.http.sdk.proxy;


import github.com.http.sdk.anno.RootApi;
import github.com.http.sdk.utils.Clean;

/**
 * @author coyzeng@gmail.com
 */
public final class HttpProxy implements Proxy {

    private Invoker<?> invoker;

    private HttpProxy(Class<? extends Invoker<?>> invoker) {
        this.invoker = Clean.newIns(invoker);
    }

    public static Proxy create() {
        return new HttpProxy(ApacheHttpInvoker.class);
    }

    public static Proxy create(Class<? extends Invoker<?>> invoker) {
        return new HttpProxy(invoker);
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> T target() {
        return (T) this.invoker;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T proxy(Class<T> service) {
        RootApi remote = service.getAnnotation(RootApi.class);
        if (null == remote) {
            throw new UnsupportedOperationException("Remote request interface must annotated with @RootApi.");
        }
        return (T) java.lang.reflect.Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service}, new HttpInvoker(remote, invoker));
    }
}

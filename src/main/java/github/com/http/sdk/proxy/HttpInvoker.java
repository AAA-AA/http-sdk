package github.com.http.sdk.proxy;

import github.com.http.sdk.anno.Http;
import github.com.http.sdk.anno.RootApi;
import github.com.http.sdk.exception.HttpException;
import github.com.http.sdk.utils.Clean;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : hongqiangren.
 * @since: 2018/6/12 23:51
 */
public class HttpInvoker implements InvocationHandler {

    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();

    static {
        try {
            Field allowedModes = MethodHandles.Lookup.class.getDeclaredField("allowedModes");
            if (Modifier.isFinal(allowedModes.getModifiers())) {
                final Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(allowedModes, allowedModes.getModifiers() & ~Modifier.FINAL);
                allowedModes.setAccessible(true);
                allowedModes.set(lookup, -1);
            }
        } catch (Exception e) {
            //
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> c = method.getDeclaringClass();
        if (c == Object.class) {
            return method.invoke(this, args);
        }
        if (method.isDefault()) {
            return lookup.in(c).unreflectSpecial(method, c).bindTo(proxy).invokeWithArguments(args);
        }
        Http http = method.getAnnotation(Http.class);
        if (null == http) {
            throw new IllegalStateException("Remote request method must annotated with @Http.");
        }
        RootApi dr = c.getAnnotation(RootApi.class);
        RootApi rr = null == dr ? this.rootApi : dr;
        if (Clean.isBlank(rr.root()) && Clean.isBlank(http.path())) {
            throw new IllegalStateException("@Remote root(x) and @Http path can not be blank together.");
        }

        Execution<?> execution = getExecution(rr);
        Invocation invocation = new HttpInvocation(execution, method, args);
        try {
            return execution.execute(invocation);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw e;
            }
            Class<?>[] dcs = method.getExceptionTypes();
            if (Arrays.stream(dcs).anyMatch(ec -> ec.isAssignableFrom(e.getClass()))) {
                throw e;
            }
            throw new HttpException(e.getMessage(), e);
        }
    }

    private Execution<?> getExecution(RootApi remote) {
        if (null == x.get(remote)) {
            x.put(remote, createHttpExecution(remote));
        }
        return x.get(remote);
    }

    private Execution<?> createHttpExecution(RootApi remote) {
        String root = remote.root();
        Execution<?> execution = HttpExecution.HttpExecutionBuilder.create(this.invoker)
                .baseUrl(root)
                .resultHandler(Clean.isEmpty(remote.result()) ? null : remote.result()[0])
                .serialize(Clean.isEmpty(remote.serializer()) ? null : remote.serializer()[0])
                .filter(remote.filter())
                .build();
        return execution;
    }

    HttpInvoker(RootApi rootApi, Invoker<?> invoker) {
        this.rootApi = rootApi;
        this.invoker = invoker;
    }
    private final RootApi rootApi;
    private final Invoker<?> invoker;
    private final Map<RootApi, Execution<?>> x = new ConcurrentHashMap<>(1);
}

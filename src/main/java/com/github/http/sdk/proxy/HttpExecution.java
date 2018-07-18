package com.github.http.sdk.proxy;

import com.github.http.sdk.exception.HttpException;
import com.github.http.sdk.filter.Filter;
import com.github.http.sdk.filter.HttpLogFilter;
import com.github.http.sdk.filter.HttpRetryFilter;
import com.github.http.sdk.handler.DefaultParamSerializer;
import com.github.http.sdk.handler.DefaultResultHandler;
import com.github.http.sdk.handler.ParamSerializer;
import com.github.http.sdk.handler.ResultHandler;
import com.github.http.sdk.utils.Clean;
import org.apache.http.client.RedirectException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : hongqiangren.
 * @since: 2018/6/13 00:03
 */
public class HttpExecution implements Execution<Object>, Invoker<Object> {
    private String               server           = "http://localhost";
    private ResultHandler resultHandler    = new DefaultResultHandler();
    private ParamSerializer httpSerializer   = new DefaultParamSerializer();
    private Invoker<?>           chainInvoker;
    private Invoker<?>           httpInvoker;

    private Invoker<?> buildInvokerChain(final Invoker<?> invoker, List<Filter> filters) {
        if (filters.size() < 1) {
            return invoker;
        }
        Invoker<?> last = invoker;
        for (int i = filters.size() - 1; i >= 0; i--) {
            final Filter filter = filters.get(i);
            final Invoker<?> next = last;
            last = new Invoker<Object>() {
                @Override
                public Object invoke(Invocation invocation) throws Throwable {
                    return filter.invoke(next, invocation);
                }
                @Override
                public String toString() {
                    return invoker.toString();
                }
            };
        }
        return last;
    }


    @Override
    public Object execute(Invocation invocation) throws Throwable {
        try {
            return chainInvoker.invoke(invocation);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Invoker<?> getInvoker() {
        return this.httpInvoker;
    }

    @Override
    public String getServer() {
        return this.server;
    }

    @Override
    public ResultHandler getResultHandler() {
        return this.resultHandler;
    }

    @Override
    public ParamSerializer getHttpSerializer() {
        return this.httpSerializer;
    }

    @Override
    public Object invoke(Invocation invocation) throws Throwable {
        try {
            byte[] o = (byte[]) this.httpInvoker.invoke(invocation);
            return this.resultHandler.handlePrint(invocation, o);
        } catch (RedirectException e) {
            return null;
        }
    }

    static final class HttpExecutionBuilder {

        private HttpExecution e = new HttpExecution();
        private List<Class<? extends Filter>> fcs = new ArrayList<>();

        private HttpExecutionBuilder(Invoker<?> invoker) {
            this.e.httpInvoker = invoker;
        }

        public static HttpExecutionBuilder create(Invoker<?> invoker) {
            return new HttpExecutionBuilder(invoker);
        }

        public HttpExecutionBuilder baseUrl(String baseUrl) {
            this.e.server = baseUrl;
            return this;
        }

        public HttpExecutionBuilder resultHandler(Class<? extends ResultHandler> handler) {
            if (null == handler || handler.isInterface()) {
                return this;
            }
            this.e.resultHandler = Clean.newIns(handler);
            return this;
        }

        public HttpExecutionBuilder filter(Class<? extends Filter>[] filters) {
            if (Clean.isEmpty(filters)) {
                return this;
            }
            fcs.addAll(Arrays.asList(filters));
            return this;
        }

        public HttpExecutionBuilder serialize(Class<? extends ParamSerializer> serialize) {
            if (null == serialize || serialize.isInterface()) {
                return this;
            }
            this.e.httpSerializer = Clean.newIns(serialize);
            return this;
        }

        public Execution<?> build() {
            if (null == this.e.httpInvoker) {
                throw new HttpException("Must appoint the http invoker implements with Invoker<?>.");
            }
            this.fcs.add(HttpRetryFilter.class);
            this.fcs.add(HttpLogFilter.class);
            List<Filter> filters = this.fcs.stream().distinct().map(filter -> Clean.newIns(filter)).collect(Collectors.toList());
            this.e.chainInvoker = this.e.buildInvokerChain(this.e, filters);
            return this.e;
        }
    }
}

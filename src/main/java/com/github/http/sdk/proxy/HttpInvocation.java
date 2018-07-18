package com.github.http.sdk.proxy;

import com.github.http.sdk.anno.HttpParam;
import github.com.http.sdk.anno.Http;
import github.com.http.sdk.anno.HttpParam;
import github.com.http.sdk.anno.RootApi;
import github.com.http.sdk.anno.TempParam;
import github.com.http.sdk.builder.IRequestBody;
import github.com.http.sdk.builder.IRequestQuery;
import github.com.http.sdk.builder.RequestBody;
import github.com.http.sdk.builder.RequestQuery;
import github.com.http.sdk.handler.ParamSerializer;
import github.com.http.sdk.utils.Clean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : hongqiangren.
 * @since: 2018/6/13 00:22
 */
public class HttpInvocation implements Invocation {


    private Http http;
    private Invoker<?> invoker;
    private IRequestQuery requestQuery;
    private IRequestBody requestBody;
    private ParamSerializer serializer;
    private Object[] _args;
    private Method method;
    private String keywords;
    private Map<String, Object> args = new HashMap<>();
    private Map<String, String> head = new HashMap<>();
    private Map<String, Object> tran = new HashMap<>(1);
    private Map<String, Object> keys = new HashMap<>(1);

    public HttpInvocation(Execution<?> execution, Method method, Object[] arguments) {
        Http http = method.getAnnotation(Http.class);
        String url = combinePath(execution.getServer(), http.path());
        this.requestQuery = new RequestQuery(url, http.urlencode());
        this.serializer = execution.getHttpSerializer();
        this._args = arguments;
        this.method = method;
        this.http = http;
        this.invoker = execution.getInvoker();

        Parameter[] pas = method.getParameters();
        RootApi remote = annotationOf(RootApi.class, 0);
        if (Clean.isEmpty(pas)) {
            this.requestBody = new RequestBody(remote, http, serializer, args);
            return;
        }
        for (int i = 0; i < pas.length; i++) {
            if (null == arguments[i]) {
                continue;
            }
            if (pas[i].isAnnotationPresent(TempParam.class)) {
                this.tran.put(pas[i].getName(), arguments[i]);
                continue;
            }
            if (pas[i].isAnnotationPresent(HttpParam.class)) {
                HttpParam httpParam = pas[i].getAnnotation(HttpParam.class);
                this.args.put(httpParam.name(),arguments[i]);
                continue;
            }
            if (Http.WRAP == http.body() || http.request().oneOf(Http.Content.XML) || Clean.isBasicType(pas[i].getType())) {
                this.args.put(pas[i].getName(), arguments[i]);
                continue;
            }
        }
        this.requestBody = new RequestBody(remote, http, serializer, args);
    }

    private String combinePath(String server, String path) {
        if (Clean.isNotBlank(path) && (path.startsWith("http://") || path.startsWith("https://"))) {
            return path;
        }
        boolean repeat = server.endsWith("/") && path.startsWith("/");
        boolean leaser = Clean.isNotBlank(path) && !server.endsWith("/") && !path.startsWith("/");
        return (repeat ? (server + path.substring(1)) : (leaser ? (server + "/" + path) : server + path)).intern();
    }

    @Override
    public Invoker<?> getProxy() {
        return this.invoker;
    }

    @Override
    public Class<?> getInterface() {
        return this.method.getDeclaringClass();
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public Object[] getParameters() {
        return new Object[0];
    }

    @Override
    public IRequestQuery addQuery(String key, Object value) {
        return this.requestQuery.addQuery(key, value);
    }

    @Override
    public Type returnType() {
        return this.method.getGenericReturnType();
    }

    @Override
    public String keywords() {
        return null == this.keywords ? "" : this.keywords;
    }

    @Override
    public <T> T getTransient(String key) {
        return (T) tran.get(key);
    }

    @Override
    public <T> T getKey(String key) {
        return (T) keys.get(key);
    }

    @Override
    public void redirect(String location) {
        this.requestQuery = new RequestQuery(location, http.urlencode());
    }

    @Override
    public IRequestBody getArgs() {
        return this.requestBody;
    }

    @Override
    public IRequestQuery url() {
        if (this.http.method().oneOf(Http.Method.POST, Http.Method.PUT) || Clean.isEmpty(this.args)) {
            return this.requestQuery;
        }
        IRequestQuery url = this.requestQuery.clone();
        this.args.entrySet().stream().forEach(kv -> url.addQuery(kv.getKey(), kv.getValue()));
        return url;
    }

    @Override
    public Map<String, String> getHeaders() {
        return this.head;
    }

    @Override
    public Map<String, String> addHeaders(String key, String value) {
        this.head.put(key, value);
        return this.head;
    }

    @Override
    public <A extends Annotation> A annotationOf(Class<A> a, int level) {
        if (TYPE == level) {
            return this.method.getDeclaringClass().getAnnotation(a);
        }
        return this.method.getAnnotation(a);
    }
}

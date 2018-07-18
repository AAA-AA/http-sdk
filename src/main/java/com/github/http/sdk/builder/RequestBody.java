package com.github.http.sdk.builder;

import com.github.http.sdk.anno.Http;
import com.github.http.sdk.anno.RootApi;
import com.github.http.sdk.handler.ParamSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author : hongqiangren.
 * @since: 2018/6/13 00:25
 */
public class RequestBody implements IRequestBody{
    private RootApi remote;
    private Http http;
    private ParamSerializer serializer;
    private Map<String, Object> arguments;

    public RequestBody(RootApi remote, Http http, ParamSerializer serializer, Map<String, Object> args) {
        this.remote = remote;
        this.http = http;
        this.serializer = serializer;
        this.arguments = args;
    }

    @Override
    public String serialize() {
        boolean onlyValue = http.body() == Http.VALUE | http.request().oneOf(Http.Content.XML);
        if (!onlyValue) {
            return this.serializer.serialize(this.http, this.arguments);
        }
        return this.serializer.serialize(this.http, this.arguments.values().stream().findFirst().orElse(null));
    }

    @Override
    public IRequestBody put(String key, Object value) {
        this.arguments.put(key, value);
        return this;
    }

    @Override
    public IValue get(String key) {
        Object val = arguments.get(key);
        if (null != val) {
            return new Value(http, serializer, val);
        }
        return new Value(http, serializer, val);
    }

    @Override
    public Set<Map.Entry<String, IValue>> entrySet() {
        Map<String, IValue> news = new HashMap<>(this.arguments.size());
        this.arguments.forEach((key, value) -> news.put(key, new Value(http, serializer, value)));
        return news.entrySet();
    }

    @Override
    public Map<String, Object> getThis() {
        return this.arguments;
    }

    @Override
    public IRequestBody reset(Object value) {
        return this;
    }
}

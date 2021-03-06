package com.github.http.sdk.builder;

import com.github.http.sdk.anno.Http;
import com.github.http.sdk.handler.ParamSerializer;

/**
 * @author : hongqiangren.
 * @since: 2018/6/13 00:14
 */
public class Value implements IValue{

    private Http http;
    private ParamSerializer serializer;
    private Object value;


    public Value(Http http, ParamSerializer serializer, Object value) {
        this.http = http;
        this.serializer = serializer;
        this.value = value;
    }


    @Override
    public Object get() {
        return this.value;
    }

    @Override
    public String serialize() {
        return  this.serializer.serialize(http, this.value);
    }
}

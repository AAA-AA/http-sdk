package com.github.http.sdk.handler;

import com.github.http.sdk.anno.Http;
import com.github.http.sdk.anno.Key;
import com.github.http.sdk.protocol.XML;
import github.com.http.sdk.anno.Http;
import github.com.http.sdk.protocol.XML;
import github.com.http.sdk.anno.Key;
import github.com.http.sdk.proxy.Invocation;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * @author : hongqiangren.
 * @since: 2018/6/13 00:03
 */
public class DefaultResultHandler implements ResultHandler{
    @Override
    public Object handle(Invocation invocation, byte[] response) throws Throwable {
        Http http = invocation.annotationOf(Http.class, 1);
        Key key = invocation.annotationOf(Key.class, 1);
        Type rtype = invocation.returnType();
        if (http.response() == Http.Content.JSON) {
            return cutJson(invocation, key, response, rtype);
        } else if (http.response() == Http.Content.XML) {
            if (Class.class.isAssignableFrom(rtype.getClass())) {
                return XML.unmarshal(response, (Class<?>) rtype);
            } else {
                return XML.unmarshal(response, (Class<?>) ((ParameterizedType) rtype).getRawType());
            }
        } else if (http.response() == Http.Content.TEXT) {
            return new String(response, Charset.defaultCharset());
        } else {
            return response;
        }
    }
}

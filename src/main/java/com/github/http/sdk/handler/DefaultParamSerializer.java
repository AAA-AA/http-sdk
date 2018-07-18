package com.github.http.sdk.handler;

import com.alibaba.fastjson.JSON;
import com.github.http.sdk.anno.Http;
import com.github.http.sdk.protocol.XML;
import github.com.http.sdk.anno.Http;
import github.com.http.sdk.protocol.XML;
import github.com.http.sdk.utils.Clean;

/**
 * @author : hongqiangren.
 * @since: 2018/6/12 23:58
 */
public class DefaultParamSerializer implements ParamSerializer {
    @Override
    public String serialize(Http http, Object o) {
        if (Clean.isBasicType(o.getClass())) {
            return String.valueOf(o);
        }
        if (http.request().oneOf(Http.Content.JSON)) {
            return JSON.toJSONString(o);
        }
        if (http.request().oneOf(Http.Content.XML)) {
            return Clean.uncheck(() -> XML.toString(o));
        }
        return JSON.toJSONString(o);
    }
}

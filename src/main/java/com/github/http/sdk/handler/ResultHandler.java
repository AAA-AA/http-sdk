package com.github.http.sdk.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.http.sdk.anno.Key;
import com.github.http.sdk.exception.HttpException;
import com.github.http.sdk.proxy.Invocation;
import com.github.http.sdk.utils.Clean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.Transient;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * @author : hongqiangren.
 * @since: 2018/6/12 23:55
 */
public interface ResultHandler {
    Object handle(Invocation invocation, byte[] response) throws Throwable;

    static Logger logger = LoggerFactory.getLogger("HTTP请求响应输出");

    default Object handlePrint(Invocation invocation, byte[] response) throws Throwable {
        if (null == invocation.annotationOf(Transient.class, Invocation.METHOD)) {
            logger.debug("keywords:{}-response:{}", invocation.keywords(), new String(response, Charset.defaultCharset()));
        }
        return handle(invocation, response);
    }

    default Object cutJson(Invocation invocation, Key ka, byte[] response, Type rtype) {
        if (null == ka || Clean.isBlank(ka.value())) {
            return JSON.parseObject(response, rtype);
        }
        String key = ka.value();
        boolean basic = rtype instanceof Class<?> && Clean.isBasicType((Class<?>) rtype);
        JSONObject json = JSON.parseObject(response, JSONObject.class);
        if (!key.contains(".")) {
            if (basic) {
                return json.getObject(key, rtype);
            } else {
                return JSON.parseObject(json.getString(key), rtype);
            }
        }
        String[] keys = key.split("\\.");
        for (int i = 0; i < keys.length; i++) {
            if (i == keys.length - 1) {
                if (basic) {
                    return json.getObject(keys[i], rtype);
                } else {
                    return JSON.parseObject(json.getString(keys[i]), rtype);
                }
            }
            if (null == json.get(keys[i])) {
                return null;
            }
            json = json.getJSONObject(keys[i]);
        }
        throw new HttpException("{}-不存在结果集", invocation.keywords());
    }
}

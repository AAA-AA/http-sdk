package com.github.http.sdk.builder;

import java.util.Map;
import java.util.Set;

/**
 * @author : hongqiangren.
 * @since: 2018/6/13 00:13
 */
public interface IRequestBody {
    String serialize();

    IRequestBody put(String key, Object value);

    IValue get(String key);

    Set<Map.Entry<String, IValue>> entrySet();

    Map<String, Object> getThis();

    IRequestBody reset(Object value);
}

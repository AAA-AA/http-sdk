package com.github.http.sdk.builder;

/**
 * @author : hongqiangren.
 * @since: 2018/6/13 00:23
 */
public interface IRequestQuery {

    IRequestQuery clone();

    String toString();

    IRequestQuery addQuery(String key, Object value);
}

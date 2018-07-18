package com.github.http.sdk.proxy;

/**
 * @author : hongqiangren.
 * @since: 2018/6/12 22:04
 */
public interface Proxy {


    <T> T target();

    <T> T proxy(Class<T> target);
}

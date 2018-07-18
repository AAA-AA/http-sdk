package com.github.http.sdk.anno;

import com.github.http.sdk.filter.Filter;
import com.github.http.sdk.handler.DefaultParamSerializer;
import com.github.http.sdk.handler.DefaultResultHandler;
import com.github.http.sdk.handler.ParamSerializer;
import com.github.http.sdk.handler.ResultHandler;
import com.github.http.sdk.ssl.SSLHandler;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author : hongqiangren.
 * @since: 2018/6/12 18:15
 */
@Inherited
@Documented
@Target({TYPE, TYPE_USE})
@Retention(RUNTIME)
public @interface RootApi {
    /**
     * root path
     */
    String root();

    /**
     * request filters
     */
    Class<? extends Filter>[] filter() default {};


    /**
     * bean serialize to string
     */
    Class<? extends ParamSerializer>[] serializer() default DefaultParamSerializer.class;
    /**
     * ssl cert support
     */
    Class<? extends SSLHandler>[] ssl() default {};

    /**
     * binary result handler
     */
    Class<? extends ResultHandler>[] result() default DefaultResultHandler.class;

    /**
     * request connection from pool time out
     */
    int borrowTimeout() default 10000;

    /**
     * connect to remote server time out
     */
    int connectTimeout() default 15000;

    /**
     * read response time out
     */
    int readTimeout() default 20000;

    /**
     * client pool size. if pools is one, it will not use connection manager.
     */
    int pools() default 10;

    /**
     * use the default client pool
     */
    boolean defaultPool() default false;


}

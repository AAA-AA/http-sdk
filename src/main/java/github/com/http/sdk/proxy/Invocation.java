package github.com.http.sdk.proxy;

import github.com.http.sdk.builder.IRequestBody;
import github.com.http.sdk.builder.IRequestQuery;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author : hongqiangren.
 * @since: 2018/6/12 22:40
 */
public interface Invocation {

    Invoker<?> getProxy();

    /**
     * invoke interface
     */
    Class<?> getInterface();

    /**
     * invoke method
     */
    Method getMethod();

    /**
     * invoke parameters
     */
    Object[] getParameters();

    /**
     * add query parameter
     */
    IRequestQuery addQuery(String key, Object value);

    /**
     * return type
     */
    Type returnType();

    /**
     * request keywords
     */
    String keywords();

    /**
     * get transient parameter
     */
    <T> T getTransient(String key);

    /**
     * get key parameter
     */
    <T> T getKey(String key);

    /**
     * reconstruct a redirect request invocation
     */
    void redirect(String location);

    /**
     * request body payload
     */
    IRequestBody getArgs();

    /**
     * url
     */
    IRequestQuery url();

    /**
     * request header
     */
    Map<String, String> getHeaders();

    /**
     * add request header
     */
    Map<String, String> addHeaders(String key, String value);


    /**
     * level {@link #TYPE}:type {@link #METHOD}:getMethod
     */
    <A extends Annotation> A annotationOf(Class<A> a, int level);

    int TYPE = 0;
    int METHOD = 1;
}

package github.com.http.sdk.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author : hongqiangren.
 * @since: 2018/6/12 18:04
 */
@Inherited
@Documented
@Target({METHOD, TYPE, TYPE_USE})
@Retention(RUNTIME)
public @interface Http {


    /**
     * limit per second. ignore that rather than 100.
     */
    int limit() default Integer.MAX_VALUE;
    /**
     * http getMethod
     */
    Method method() default Method.GET;

    /**
     * encode all of the url parameters
     */
    boolean urlencode() default false;

    /**
     * fault retry times
     */
    int retries() default 3;


    /**
     * fault delay millions. ignore that rather than 20000.
     */
    int delay() default 2000;

    /**
     * request content
     */
    Content request() default Content.JSON;

    /**
     * response content
     */
    Content response() default Content.JSON;

    /**
     * relative path to the server
     */
    String path() default "";

    /**
     * {@link #MERGE} deconstruct all parameter object to a map
     * {@link #WRAP} otherwise wrap all parameters in a map object
     * {@link #VALUE} only use the first valid value without key.
     */
    int body() default WRAP;

    int MERGE = -1;
    int WRAP = 0;
    int VALUE = 1;


    enum Method {
        GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE;

        public boolean oneOf(Http.Method... methods) {
            if (null == methods || methods.length < 1) {
                return false;
            }
            for (Http.Method method : methods) {
                if (this == method) {
                    return true;
                }
            }
            return false;
        }
    }

    enum Content {
        JSON, FORM, XML, TEXT, STREAM;

        public boolean oneOf(Http.Content... contents) {
            if (null == contents || contents.length < 1) {
                return false;
            }
            for (Http.Content content : contents) {
                if (this == content) {
                    return true;
                }
            }
            return false;
        }
    }

}


package com.github.http.sdk.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author : hongqiangren.
 * @since: 2018/6/12 23:56
 */
@Inherited
@Documented
@Target({FIELD, PARAMETER, METHOD, TYPE_USE})
@Retention(RUNTIME)
public @interface Key {
    /**
     * annotated in parameter. will print in the log or cause message.<br/>
     * annotated in method. will extract the root of key to return.
     *
     * @return
     */
    String value() default "";
}

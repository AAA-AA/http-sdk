package github.com.http.sdk.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author : hongqiangren.
 * @since: 2018/6/14 17:23
 */
@Inherited
@Documented
@Target({FIELD, PARAMETER, METHOD})
@Retention(RUNTIME)
public @interface TempParam {
}

package github.com.http.sdk.anno;

import github.com.http.sdk.spring.HttpConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author : hongqiangren.
 * @since: 2018/7/18 13:43
 */
@Inherited
@Documented
@Target({TYPE, TYPE_USE})
@Retention(RUNTIME)
@Import({HttpConfig.class})
public @interface EnableHttp {

}

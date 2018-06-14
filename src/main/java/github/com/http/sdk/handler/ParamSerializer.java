package github.com.http.sdk.handler;

import github.com.http.sdk.anno.Http;

/**
 * @author : hongqiangren.
 * @since: 2018/6/12 23:58
 */
public interface ParamSerializer {
    String serialize(Http http, Object o);
}

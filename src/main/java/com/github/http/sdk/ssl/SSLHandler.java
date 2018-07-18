package com.github.http.sdk.ssl;

import javax.net.ssl.SSLContext;

/**
 * @author : hongqiangren.
 * @since: 2018/6/12 19:23
 */
public interface SSLHandler {

    SSLContext createSSL();
}

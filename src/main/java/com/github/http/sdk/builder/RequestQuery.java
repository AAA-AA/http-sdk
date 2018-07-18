package com.github.http.sdk.builder;


import com.github.http.sdk.utils.Clean;

import java.net.URLEncoder;

/**
 * @author : hongqiangren.
 * @since: 2018/6/13 00:23
 */
public class RequestQuery implements IRequestQuery, Cloneable{
    private boolean encode = false;
    private StringBuilder location;

    public RequestQuery(String location, boolean encode) {
        this.location = new StringBuilder(location);
        this.encode = encode;
        if (!location.contains("?")) {
            this.location.append('?');
        }
    }

    @Override
    public IRequestQuery addQuery(String key, Object value) {
        char last = this.location.charAt(this.location.length() - 1);
        if (last != '&' && last != '?') {
            this.location.append('&');
        }
        this.location.append(key).append('=').append(String.valueOf(value)).append('&');
        return this;
    }

    @Override
    public IRequestQuery clone() {
        IRequestQuery requestQuery = new RequestQuery(toString(), this.encode);
        return requestQuery;
    }

    @Override
    public String toString() {
        char last = this.location.charAt(this.location.length() - 1);
        boolean cut = last == '&' || last == '?';
        final String url = cut ? this.location.substring(0, this.location.length() - 1) : this.location.toString();
        return encode ? Clean.uncheck(() -> URLEncoder.encode(url, "UTF-8")) : url;
    }
}

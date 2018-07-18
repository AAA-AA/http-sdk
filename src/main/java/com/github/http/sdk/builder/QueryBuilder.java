package com.github.http.sdk.builder;

import com.github.http.sdk.anno.Http;
import github.com.http.sdk.anno.Http;
import github.com.http.sdk.proxy.Invocation;
import github.com.http.sdk.utils.Clean;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : hongqiangren.
 * @since: 2018/6/12 23:04
 */
public final class QueryBuilder {

    public static List<Header> buildHeader(Map<String, String> headers)

    {
        return headers.entrySet().stream().map(kv -> new BasicHeader(kv.getKey(), kv.getValue())).collect(Collectors.toList());
    }

    public static List<BasicNameValuePair> buildForm(Invocation invocation)

    {
        return invocation.getArgs().entrySet().stream().map(kv -> new BasicNameValuePair(kv.getKey(), kv.getValue().serialize())).collect(Collectors.toList());
    }

    public static HttpEntity buildEntity(Invocation invocation) {
        Http http = invocation.annotationOf(Http.class, 1);
        if (http.request().oneOf(Http.Content.JSON)) {
            return new StringEntity(invocation.getArgs().serialize(), JTYPE);
        }
        if (http.request().oneOf(Http.Content.FORM)) {
            List<BasicNameValuePair> nvs = buildForm(invocation);
            return Clean.uncheck(() -> new UrlEncodedFormEntity(nvs, Charset.defaultCharset()));
        }
        if (http.request().oneOf(Http.Content.XML)) {
            return new StringEntity(invocation.getArgs().serialize(), XTYPE);
        }
        if (http.request().oneOf(Http.Content.STREAM, Http.Content.TEXT)) {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            invocation.getArgs().entrySet().forEach(kv -> {
                if (kv.getValue().get().getClass() == byte[].class) {
                    builder.addBinaryBody(kv.getKey(), (byte[]) kv.getValue().get(), BTYPE, Clean.randomNumeric(10) + ".png");
                } else {
                    builder.addTextBody(kv.getKey(), kv.getValue().serialize());
                }
            });
            return builder.setContentType(BTYPE).build();
        }
        return new StringEntity(invocation.getArgs().serialize(), JTYPE);
    }

    private static final List<Header> defaultHeaders = new ArrayList<>();

    static {
        defaultHeaders.add(new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, sdch"));
        defaultHeaders.add(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "en-US,en;q=0.8,zh-CN;q=0.6,zh;q=0.4"));
        defaultHeaders.add(new BasicHeader(HttpHeaders.CACHE_CONTROL, "max-age=0"));
        defaultHeaders.add(new BasicHeader(HttpHeaders.CONNECTION, "keep-alive"));
        defaultHeaders.add(new BasicHeader(HttpHeaders.UPGRADE, "1"));
        defaultHeaders.add(new BasicHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36"));
    }

    static final ContentType JTYPE = ContentType.create("application/json", Charset.forName("UTF-8"));
    static final ContentType FTYPE = ContentType.create("application/x-www-form-urlencoded", Charset.forName("UTF-8"));
    static final ContentType XTYPE = ContentType.create("application/xml", Charset.forName("UTF-8"));
    static final ContentType BTYPE = ContentType.create("multipart/form-data", Charset.forName("UTF-8"));

    public static Header[] buildHeader(Invocation invocation) {
        Map<String, String> header = invocation.getHeaders();
        List<Header> hds = buildHeader(header);
        hds.addAll(defaultHeaders);
        Http http = invocation.annotationOf(Http.class, 1);
        if (http.request().oneOf(Http.Content.JSON)) {
            hds.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, JTYPE.toString()));
        }
        if (http.request().oneOf(Http.Content.FORM)) {
            hds.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, FTYPE.toString()));
        }
        if (http.request().oneOf(Http.Content.XML)) {
            hds.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, XTYPE.toString()));
        }
        if (http.request().oneOf(Http.Content.STREAM, Http.Content.TEXT)) {
            hds.add(new BasicHeader(HttpHeaders.CONTENT_TYPE, BTYPE.toString()));
        }
        return hds.toArray(new Header[hds.size()]);
    }




}

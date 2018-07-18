package com.github.http.sdk.utils;

import com.github.http.sdk.anno.HttpParam;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.*;

/**
 *
 *
 * 提供httpClient的通用实现，当使用非代理模式时，可以使用该工具类
 *
 *
 * @author : hongqiangren.
 * @since: 2018/6/11 21:36
 */
public class HttpClientApi {

    /**
     * 获取连接的最大等待时间
     */
    public final static int DEFAULT_WAIT_TIMEOUT = 60000;

    /**
     * 连接超时时间
     */
    public final static int DEFAULT_CONNECT_TIMEOUT = 10000;

    /**
     * 读取超时时间
     */
    public final static int DEFAULT_READ_TIMEOUT = 5000;

    private final HttpClient httpClient;

    private final static HttpClientBuilder clientBuilder = HttpClientBuilder.create();

    public final static RequestConfig DEFAULT_CONFIG = RequestConfig.custom()
            .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
            .setConnectionRequestTimeout(DEFAULT_WAIT_TIMEOUT)
            .setSocketTimeout(DEFAULT_READ_TIMEOUT)
            .build();

    private static final HttpClient DEFAULT_HTTP_CLIENT = HttpClientBuilder.create()
            .setDefaultRequestConfig(DEFAULT_CONFIG).build();

    public HttpClientApi(HttpClient httpClient) {
        this.httpClient = httpClient == null ? DEFAULT_HTTP_CLIENT : httpClient;

    }

    public HttpClientApi() {
        this.httpClient = DEFAULT_HTTP_CLIENT;
    }

    public HttpClientApi(RequestConfig config) {
        this.httpClient = config == null ? DEFAULT_HTTP_CLIENT : clientBuilder.setDefaultRequestConfig(config).build();
    }


    public byte[] getBytes(String baseUrl, List<NameValuePair> params, Header... headers) {
        return null;
    }


    public byte[] getBytes(String url, List<NameValuePair> params) {
        return null;
    }

    public String get(String url, Map<String, String> params) throws HttpException {

        return get(url, convertToList(params), null);
    }


    public String get(String url, List<NameValuePair> params) throws HttpException {
        return get(url, params, null);
    }

    public String get(String url, final Object param) throws  HttpException {
        Map<String, String> paramMap = convertToMap(param);

        return get(url,paramMap);
    }

    private static Map<String, String> convertToMap(Object param)  {
        Field[] fields = param.getClass().getDeclaredFields();
        Map<String, String> paramMap = new HashMap<>(fields.length);
        for (Field field : fields) {
            field.setAccessible(true);
            Object object;
            try {
                object = field.get(param);
                HttpParam jsonField = field.getDeclaredAnnotation(HttpParam.class);
                if (object != null) {
                    if (jsonField != null) {
                        paramMap.put(jsonField.name(), object.toString());
                    } else {
                        paramMap.put(field.getName(), object.toString());
                    }
                }
            } catch (IllegalAccessException e) {

            }
        }
        return paramMap;
    }

    public String get(String url, List<NameValuePair> params, Header... headers) throws HttpException {
        HttpGet httpGet;
        HttpEntity responseEntity;
        String content = "";
        String queryString = "";
        try {
            if (params != null && params.size() > 0) {
                queryString = EntityUtils.toString(new UrlEncodedFormEntity(params, "UTF-8"));
            }
            httpGet = new HttpGet(String.format("%s?%s", url, queryString));
            if (headers != null) {
                httpGet.setHeaders(headers);
            }
            HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            responseEntity = response.getEntity();
            if (responseEntity != null) {
                content = EntityUtils.toString(responseEntity,"UTF-8");
            }
            if (statusCode != 200) {
                throw new HttpException("请求目标系统发生异常，请求url:" + url + ",响应码：" + statusCode + ",内容:" + content);
            }
            return content;

        } catch (IOException e) {
            throw new HttpException("请求目标系统发生异常，请求url:" + url);
        }
    }

    private List<NameValuePair> convertToList(Map<String, String> params) {
        if (params != null) {
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                NameValuePair nameValuePair = new BasicNameValuePair(entry.getKey(), entry.getValue());
                list.add(nameValuePair);
            }
            return list;
        }
        return null;
    }

    public String post(String url, Map<String, String> params) throws HttpException {
        return post(url, convertToList(params));
    }

    public String post(String url, List<NameValuePair> params) throws HttpException {
        return post(url, params, null, null);
    }

    public String post(String url, List<NameValuePair> params, Header... headers) throws HttpException {
        return post(url, params, headers == null ? null : Arrays.asList(headers), null);
    }

    public String post(String url, List<NameValuePair> params, List<Header> headers, List<Cookie> cookies) throws HttpException {
        HttpPost httpPost = null;
        HttpEntity responseEntity = null;
        String content;
        try {
            httpPost = new HttpPost(url);
            if (params != null) {
                httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            }
            if (headers != null) {
                Header[] headerArrays = headers.toArray(new Header[headers.size()]);
                httpPost.setHeaders(headerArrays);
            }
            HttpResponse response;
            if (cookies != null) {
                BasicCookieStore cookieStore = new BasicCookieStore();
                Cookie[] cookieArrays = cookies.toArray(new Cookie[cookies.size()]);
                cookieStore.addCookies(cookieArrays);
                HttpContext localContext = new BasicHttpContext();
                localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
                response = httpClient.execute(httpPost, localContext);
            } else {
                response = httpClient.execute(httpPost);
            }
            responseEntity = response.getEntity();
            if (responseEntity != null) {
                content = EntityUtils.toString(responseEntity, "UTF-8");
                return content;
            }
            int statusCode = response.getStatusLine().getStatusCode();
            throw new HttpException("请求目标系统发生异常，请求url:" + url + ",响应码：" + statusCode);
        } catch (UnsupportedEncodingException e) {
            throw new HttpException("UnsupportedEncodingException " + url);
        } catch (IOException e) {
            throw new HttpException("IOException ", e);
        }
    }
}

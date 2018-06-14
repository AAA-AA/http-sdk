package github.com.http.sdk.core;

import github.com.http.sdk.anno.Http;
import github.com.http.sdk.anno.RootApi;
import github.com.http.sdk.exception.HttpException;
import github.com.http.sdk.factory.ClientFactory;
import github.com.http.sdk.utils.Clean;
import github.com.http.sdk.builder.QueryBuilder;
import github.com.http.sdk.handler.ApacheResponseHandler;
import github.com.http.sdk.handler.ResponseHandler;
import github.com.http.sdk.proxy.Invocation;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;

import java.net.URI;
import java.rmi.RemoteException;
import java.util.function.Supplier;

/**
 * @author : hongqiangren.
 * @since: 2018/6/12 22:46
 */
public enum HttpClient {
    single;

    private final ResponseHandler<byte[], HttpResponse> responseHandler = new ApacheResponseHandler();

    public byte[] load(Supplier<HttpRequestBase> supp, Invocation invocation) throws Throwable {
        RootApi remote = invocation.annotationOf(RootApi.class, Invocation.TYPE);
        HttpRequestBase method = supp.get();
        try {
            return ClientFactory.ins.getClient(remote).execute(method, (r) -> responseHandler.handle(invocation, r));
        } catch (HttpException e) {
            throw e;
        } catch (RemoteException e) {
            throw e;
        } catch (Throwable e) {
            throw e;
        } finally {
            method.releaseConnection();
        }
    }

    public byte[] execute(Invocation invocation) throws Throwable {
        Http http = invocation.annotationOf(Http.class, Invocation.METHOD);
        if (Clean.isBlank(invocation.url().toString())) {
            throw new HttpException(String.format("%s-Url can not be blank. ", invocation.keywords()));
        }
        return load(() -> {
            HttpRequestBase method = createMethod(http);
            if (http.method().oneOf(Http.Method.POST, Http.Method.PUT)) {
                HttpEntityEnclosingRequestBase _method = (HttpEntityEnclosingRequestBase) method;
                HttpEntity entity = QueryBuilder.buildEntity(invocation);
                _method.setEntity(entity);
            }
            method.setURI(URI.create(invocation.url().toString()));
            method.setHeaders(QueryBuilder.buildHeader(invocation));
            return method;
        }, invocation);
    }

    @SuppressWarnings("unchecked")
    private <T extends HttpRequestBase> T createMethod(Http http) {
        if (http.method() == Http.Method.POST) {
            return (T) new HttpPost();
        }
        if (http.method() == Http.Method.PUT) {
            return (T) new HttpPut();
        }
        if (http.method() == Http.Method.DELETE) {
            return (T) new HttpDelete();
        }
        if (http.method() == Http.Method.GET) {
            return (T) new HttpGet();
        }
        return (T) new HttpGet();
    }
}

package com.github.http.sdk.factory;


import com.github.http.sdk.anno.RootApi;
import com.github.http.sdk.ssl.SSLHandler;
import github.com.http.sdk.anno.RootApi;
import github.com.http.sdk.ssl.SSLHandler;
import github.com.http.sdk.utils.Clean;
import github.com.http.sdk.function.VoidExecutor;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author coyzeng@gmail.com
 */
public enum ClientFactory {
    ins;
    private final Map<RootApi, CloseableHttpClient> clients = new ConcurrentHashMap<>();
    private final CloseableHttpClient defaultClient = createClient(20, 10000, 15000, 20000, null);

    public CloseableHttpClient getClient(RootApi remote) {
        if (remote.defaultPool()) {
            return defaultClient;
        }
        if (null == clients.get(remote)) {
            clients.put(remote, createClient(remote));
        }
        return clients.get(remote);
    }

    public void release() {
        Clean.uncheck(() -> {
            defaultClient.close();
            if (!Clean.isEmpty(clients)) {
                clients.values().forEach(c -> {
                    Clean.uncheck(c::close);
                });
            }
            return true;
        });
    }

    private CloseableHttpClient createClient(RootApi remote)  {
        return createClient(
                remote.pools(),
                remote.borrowTimeout(),
                remote.connectTimeout(),
                remote.readTimeout(),
                (remote.ssl() == null || remote.ssl().length == 0) ? null : remote.ssl()[0]);
    }

    private CloseableHttpClient createClient(int poolSize,
                                             int borrowTimeout,
                                             int connectTimeout,
                                             int readTimeout,
                                             Class<? extends SSLHandler> ssl) {
        RequestConfig config  = RequestConfig.custom().setConnectTimeout(borrowTimeout)
                .setConnectionRequestTimeout(connectTimeout)
                .setSocketTimeout(readTimeout).build();

        HttpClientBuilder instance = HttpClients.custom();
        instance.setDefaultRequestConfig(config);
        instance.setDefaultCookieStore(new BasicCookieStore());

        if (poolSize > 1) {
            PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
            manager.setMaxTotal(poolSize);
            manager.setDefaultMaxPerRoute(poolSize);
            instance.setConnectionManager(manager);
            CloseableHttpClient client = instance.build();
            return client;
        }
        CloseableHttpClient client = instance.build();
        return client;
    }

    private void addShutdownHook(VoidExecutor executor) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                executor.execute();
            } catch (Exception e) {
                //
            }
        }));
    }
}

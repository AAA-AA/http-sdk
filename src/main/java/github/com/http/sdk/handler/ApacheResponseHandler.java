package github.com.http.sdk.handler;

import github.com.http.sdk.exception.HttpException;
import github.com.http.sdk.proxy.Invocation;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author : hongqiangren.
 * @since: 2018/6/12 22:55
 */
public class ApacheResponseHandler implements ResponseHandler<byte[], HttpResponse>{
    @Override
    public byte[] handle(Invocation invocation, HttpResponse response) throws IOException {
        int status = response.getStatusLine().getStatusCode();
        String cause = response.getStatusLine().getReasonPhrase();
        HttpEntity entity = response.getEntity();
        byte[] content = null == entity ? new byte[0] : EntityUtils.toByteArray(entity);
        if (status >= 200 && status < 300) {
            return content;
        }
        if (302 == status) {
            Header redirect = response.getFirstHeader("location");
            if (null == redirect) {
                return new byte[0];
            }
            throw new HttpException(redirect.getValue());
        }
        String res = new String(content, Charset.defaultCharset());
        String key = invocation.keywords();
        if (status >= 500) {
            throw new HttpException("keywords:{}, status:{}, cause:{}, content:{}", key, status, cause, res);
        } else {
            throw new HttpException("keywords:{}, status:{}, cause:{}, content:{}", key, status, cause, res);
        }
    }
}

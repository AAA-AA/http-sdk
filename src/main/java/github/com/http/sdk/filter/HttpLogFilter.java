package github.com.http.sdk.filter;

import com.alibaba.fastjson.JSON;
import github.com.http.sdk.proxy.Invocation;
import github.com.http.sdk.proxy.Invoker;
import github.com.http.sdk.utils.Clean;
import lombok.extern.slf4j.Slf4j;

import java.beans.Transient;

/**
 * @author : hongqiangren.
 * @since: 2018/6/13 00:11
 */
@Slf4j(topic = "HTTP请求日志输出")
public class HttpLogFilter implements Filter{
    @Override
    public Object invoke(Invoker<?> invoker, Invocation invocation) throws Throwable {
        try {
            String rpd = Clean.randomNumeric(5);
            String key = invocation.keywords();
            String url = invocation.url().toString();
            String arg = invocation.getArgs().serialize();
            String hea = JSON.toJSONString(invocation.getHeaders());
            log.info("关键字:{}-执行-{}.{}-{}", key, invocation.getInterface().getName(), invocation.getMethod().getName(), rpd);
            log.info("关键字:{}-地址:{}-参数:{}-头部:{}-{}", key, url, arg, hea, rpd);
            Object result = invoker.invoke(invocation);
            if (null != invocation.annotationOf(Transient.class, Invocation.METHOD)) {
                return result;
            }
            log.info("关键字:{}-地址:{}-结果:{}-{}", key, url, JSON.toJSONString(result), rpd);
            return result;
        } catch (Exception e) {
            log.error(invocation.keywords(), e);
            throw e;
        }
    }
}

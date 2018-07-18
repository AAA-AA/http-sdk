package github.com.http.sdk.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : hongqiangren.
 * @since: 2018/7/18 13:53
 */
@Configuration
public class HttpConfig {
    public HttpConfig() {
    }

    @Bean
    public HttpInstantiationAwareBeanPostProcessor httpInstantiationAwareBeanPostProcessor() {
        return new HttpInstantiationAwareBeanPostProcessor();
    }

    @Bean
    public HttpBeanFactoryPostProcessor httpBeanFactoryPostProcessor() {
        return new HttpBeanFactoryPostProcessor();
    }
}

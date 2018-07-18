package github.com.http.sdk.spring;

import github.com.http.sdk.utils.Clean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.core.Ordered;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @author : hongqiangren.
 * @since: 2018/7/18 13:54
 */
public class HttpInstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter implements Ordered {
    public HttpInstantiationAwareBeanPostProcessor() {
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        this.greedScanRemoteInterface(beanClass);
        return super.postProcessBeforeInstantiation(beanClass, beanName);
    }

    private void greedScanRemoteInterface(Class<?> beanClass) {
        if (Clean.expectGreedScan(beanClass)) {
            Field[] fields = beanClass.getDeclaredFields();
            if (!Clean.isEmpty(fields)) {
                Arrays.stream(fields).forEach((c) -> {
                    HttpBeanRegistry.INSTANCE.registerBeanDefinition(c.getType());
                });
            }
            this.greedScanRemoteInterface(beanClass.getSuperclass());
        }
    }


    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}

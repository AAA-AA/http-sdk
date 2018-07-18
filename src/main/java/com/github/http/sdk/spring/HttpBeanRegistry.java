package com.github.http.sdk.spring;

import com.github.http.sdk.anno.RootApi;
import com.github.http.sdk.proxy.HttpProxy;
import github.com.http.sdk.anno.RootApi;
import github.com.http.sdk.proxy.HttpProxy;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * @author : hongqiangren.
 * @since: 2018/7/18 13:57
 */
public enum HttpBeanRegistry {
    INSTANCE;

    private ConfigurableListableBeanFactory beanFactory;
    private boolean isDefaultListableBeanFactory;

    HttpBeanRegistry() {
    }

    void setCoreBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.isDefaultListableBeanFactory = beanFactory instanceof DefaultListableBeanFactory;
    }

    void registerBeanDefinition(Class<?> type) {
        if (type.isAnnotationPresent(RootApi.class)) {
            try {
                this.beanFactory.getBean(type);
            } catch (NoSuchBeanDefinitionException var3) {
                Object bean = HttpProxy.create().proxy(type);
                this.beanFactory.registerResolvableDependency(type, bean);
                this.beanFactory.registerSingleton(this.formatBeanName(type), bean);
            }
        }
    }

    private String formatBeanName(Class<?> type) {
        String name = type.getSimpleName();
        return name.length() > 2 && Character.isUpperCase(name.charAt(0)) && Character.isUpperCase(name.charAt(1)) ? Character.toLowerCase(name.charAt(1)) + name.substring(2) : Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }
}

package com.milloc.referfeign.springbootautoconfigure.client;

import com.milloc.referfeign.springbootautoconfigure.parameter.ParameterResolver;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gongdeming
 * @create 2019-09-05
 */
public class ReferClientFactoryBean<T> implements FactoryBean<T>, ApplicationContextAware {
    private InvocationHandler referInvocationHandler;
    private Class<T> clientInterface;

    ReferClientFactoryBean() {
    }

    public ReferClientFactoryBean(Class<T> clientInterface) {
        this.clientInterface = clientInterface;
    }

    public void setClientInterface(Class<T> clientInterface) {
        this.clientInterface = clientInterface;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getObject() {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{clientInterface},
                referInvocationHandler);
    }

    @Override
    public Class<?> getObjectType() {
        return clientInterface;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RestTemplate restTemplate = applicationContext.getBean(RestTemplate.class);
        Map<String, ParameterResolver> beansOfType = applicationContext.getBeansOfType(ParameterResolver.class);
        List<ParameterResolver> parameterResolvers = beansOfType.values().stream()
                .sorted(Comparator.comparing(ParameterResolver::getOrder))
                .collect(Collectors.toList());
        this.referInvocationHandler = new ReferInvocationHandler(restTemplate, parameterResolvers, clientInterface);
    }
}
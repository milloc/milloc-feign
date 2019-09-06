package com.milloc.dbfeignspringbootautoconfigure.client;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author gongdeming
 * @create 2019-09-05
 */
public class DBClientFactoryBean<T> implements FactoryBean<T>, ApplicationContextAware {
    private InvocationHandler dbInvocationHandler;
    private ApplicationContext applicationContext;
    private Class<T> clientInterface;

    DBClientFactoryBean() {
    }

    public DBClientFactoryBean(Class<T> clientInterface) {
        this.clientInterface = clientInterface;
    }

    public void setClientInterface(Class<T> clientInterface) {
        this.clientInterface = clientInterface;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getObject() {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{clientInterface},
                dbInvocationHandler);
    }

    @Override
    public Class<?> getObjectType() {
        return clientInterface;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        RestTemplate restTemplate = this.applicationContext.getBean(RestTemplate.class);
        this.dbInvocationHandler = new DBInvocationHandler(restTemplate);
    }
}

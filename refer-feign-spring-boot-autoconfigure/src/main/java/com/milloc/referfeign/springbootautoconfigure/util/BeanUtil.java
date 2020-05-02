package com.milloc.referfeign.springbootautoconfigure.util;

import lombok.SneakyThrows;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

public class BeanUtil {
    @SneakyThrows
    public static <T> Map<String, Object> beanToMap(T data) {
        Class<?> aClass = data.getClass();
        BeanInfo beanInfo = Introspector.getBeanInfo(aClass);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        Map<String, Object> beanMap = new LinkedHashMap<>();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String name = propertyDescriptor.getName();
            if (name.startsWith("class")) {
                continue;
            }
            Method readMethod = propertyDescriptor.getReadMethod();
            beanMap.put(name, readMethod.invoke(data));
        }
        return beanMap;
    }
}

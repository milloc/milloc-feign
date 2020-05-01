package com.milloc.referfeign.springbootautoconfigure.parameter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PathParamURIParameterResolver implements ParameterResolver, ApplicationContextAware {
    private ObjectMapper objectMapper;

    @Override
    public int getOrder() {
        return 0;
    }

    @SneakyThrows
    @Override
    public boolean resolved(RequestBuilder builder, Parameter parameter, Object value) {
        PathVariable pathVariableAnn = parameter.getAnnotation(PathVariable.class);
        UriComponentsBuilder uriComponentsBuilder = builder.getUriComponentsBuilder();
        if (pathVariableAnn == null) {
            return false;
        }

        if (value instanceof Map) {
            Map<String, Object> map = new HashMap<>();
            ((Map<?, ?>) value).forEach((k, v) -> map.put(k.toString(), v));
            uriComponentsBuilder.uriVariables(map);
            return true;
        }

        Class<?> type = parameter.getType();
        if (type.isPrimitive() || BeanUtils.isSimpleValueType(type)) {
            String n;
            Map<String, Object> map = Collections.singletonMap(
                    (n = (String) AnnotationUtils.getValue(pathVariableAnn)) == null ? parameter.getName() : n, value);
            uriComponentsBuilder.uriVariables(map);
            return true;
        }

        // pojo
        String s = objectMapper.writeValueAsString(value);
        Map<String, Object> o = objectMapper.readValue(s, new TypeReference<Map<String, Object>>() {
        });
        uriComponentsBuilder.uriVariables(o);
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.objectMapper = applicationContext.getBean(ObjectMapper.class);
    }
}

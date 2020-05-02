package com.milloc.referfeign.springbootautoconfigure.parameter;

import com.milloc.referfeign.springbootautoconfigure.client.RequestBuilder;
import com.milloc.referfeign.springbootautoconfigure.util.BeanUtil;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Order(0)
public class PathParamParameterResolver implements ParameterResolver {
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
        uriComponentsBuilder.uriVariables(BeanUtil.beanToMap(value));
        return true;
    }

}

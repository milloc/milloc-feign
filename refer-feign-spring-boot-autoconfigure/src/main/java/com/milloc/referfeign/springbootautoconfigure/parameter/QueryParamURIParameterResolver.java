package com.milloc.referfeign.springbootautoconfigure.parameter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.milloc.referfeign.springbootautoconfigure.annotation.QueryParam;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.Map;

public class QueryParamURIParameterResolver implements ParameterResolver, ApplicationContextAware {
    private ObjectMapper objectMapper;

    @Override
    public int getOrder() {
        return 0;
    }

    @SneakyThrows
    @Override
    public boolean resolved(RequestBuilder builder, Parameter parameter, Object value) {
        QueryParam annotationAnn = parameter.getAnnotation(QueryParam.class);
        HttpMethod httpMethod = builder.getHttpMethod();
        UriComponentsBuilder uriComponentsBuilder = builder.getUriComponentsBuilder();
        String n = null;
        if (annotationAnn != null) {
            n = (String) AnnotationUtils.getValue(annotationAnn);
        } else if (HttpMethod.POST.equals(httpMethod)) {
            return false;
        }
        if (n == null || "".equals(n)) {
            n = parameter.getName();
        }

        if (value instanceof Map) {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            ((Map<?, ?>) value).forEach((mK, mV) -> params.add(mK.toString(), mV.toString()));
            uriComponentsBuilder.replaceQueryParams(params);
            return true;
        }

        Object v;
        Class<?> type = parameter.getType();
        if (type.isPrimitive() || type.isArray() || BeanUtils.isSimpleValueType(type)) {
            v = value;
        } else if (Collection.class.isAssignableFrom(type)) {
            v = ((Collection<?>) value).toArray();
        } else {
            // pojo
            String json = objectMapper.writeValueAsString(value);
            v = objectMapper.readValue(json, new TypeReference<Map<String, String>>() {
            });
        }
        uriComponentsBuilder.queryParam(n, v);
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        objectMapper = applicationContext.getBean(ObjectMapper.class);
    }
}

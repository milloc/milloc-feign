package com.milloc.referfeign.springbootautoconfigure.parameter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Parameter;
import java.util.Map;

public class FormParameterResolver implements ParameterResolver, ApplicationContextAware {
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    @SuppressWarnings("unchecked")
    public boolean resolved(RequestBuilder builder, Parameter param, Object value) {
        if (HttpMethod.POST.equals(builder.getHttpMethod())) {
            MediaType contentType = builder.getHttpHeaders().getContentType();
            if (MediaType.MULTIPART_FORM_DATA.equals(contentType) || MediaType.APPLICATION_FORM_URLENCODED.equals(contentType)) {
                RequestBody annotation = param.getAnnotation(RequestBody.class);
                if (annotation != null) {
                    String json = objectMapper.writeValueAsString(value);
                    Map<Object, Object> map = objectMapper.readValue(json, new TypeReference<Map<?, ?>>() {
                    });
                    MultiValueMap<Object, Object> params = new LinkedMultiValueMap<>();
                    map.forEach(params::add);
                    builder.setBody(params);
                    return true;
                }

                MultiValueMap<Object, Object> body;
                if (builder.getBody() != null && builder.getBody() instanceof MultiValueMap) {
                    body = (MultiValueMap<Object, Object>) builder.getBody();
                } else {
                    body = new LinkedMultiValueMap<>();
                }

                body.add(param.getName(), value);
                builder.setBody(body);
                return true;
            }
        }
        return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.objectMapper = applicationContext.getBean(ObjectMapper.class);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}

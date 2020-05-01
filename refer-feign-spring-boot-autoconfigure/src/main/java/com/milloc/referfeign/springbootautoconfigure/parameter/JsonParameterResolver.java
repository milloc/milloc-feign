package com.milloc.referfeign.springbootautoconfigure.parameter;

import lombok.SneakyThrows;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.lang.reflect.Parameter;

public class JsonParameterResolver implements ParameterResolver {
    @SneakyThrows
    @Override
    public boolean resolved(RequestBuilder builder, Parameter param, Object value) {
        if (HttpMethod.POST.equals(builder.getHttpMethod()) && MediaType.APPLICATION_JSON.equals(builder.getHttpHeaders().getContentType())) {
            builder.setBody(value);
            return true;
        }
        return false;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}

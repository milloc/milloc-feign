package com.milloc.referfeign.springbootautoconfigure.parameter;

import com.milloc.referfeign.springbootautoconfigure.client.RequestBuilder;
import lombok.SneakyThrows;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.lang.reflect.Parameter;

@Order
public class JsonParameterResolver implements ParameterResolver {
    @SneakyThrows
    @Override
    public boolean resolved(RequestBuilder builder, Parameter param, Object value) {
        if (HttpMethod.POST.equals(builder.getHttpMethod()) &&
                MediaType.APPLICATION_JSON.equals(builder.getHttpHeaders().getContentType())) {
            builder.setBody(value);
            return true;
        }
        return false;
    }
}

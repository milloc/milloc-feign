package com.milloc.referfeign.springbootautoconfigure.parameter;

import com.milloc.referfeign.springbootautoconfigure.client.RequestBuilder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Order(0)
public class RequestHeaderParameterResolver implements ParameterResolver {
    @Override
    public boolean resolved(RequestBuilder builder, Parameter param, Object value) {
        RequestHeader requestHeaderAnn = param.getAnnotation(RequestHeader.class);
        if (requestHeaderAnn == null) {
            return false;
        }
        String header = (String) AnnotationUtils.getValue(requestHeaderAnn);
        if (header == null || "".equals(header)) {
            return false;
        }

        HttpHeaders httpHeaders = builder.getHttpHeaders();
        if (value instanceof List) {
            List<String> list = new ArrayList<>(((List<?>) value).size());
            ((List<?>) value).forEach(v -> list.add(v.toString()));
            httpHeaders.addAll(header, list);
            return true;
        }
        if (value instanceof Map) {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            ((Map<?, ?>) value).forEach((k, v) -> map.add(k.toString(), v.toString()));
            httpHeaders.addAll(map);
            return true;
        }
        if (value instanceof String) {
            httpHeaders.set(header, (String) value);
            return true;
        }
        return false;
    }
}

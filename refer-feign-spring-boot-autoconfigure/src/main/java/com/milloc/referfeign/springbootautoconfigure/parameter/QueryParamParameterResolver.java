package com.milloc.referfeign.springbootautoconfigure.parameter;

import com.milloc.referfeign.springbootautoconfigure.annotation.QueryParam;
import com.milloc.referfeign.springbootautoconfigure.client.RequestBuilder;
import com.milloc.referfeign.springbootautoconfigure.util.BeanUtil;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Order(0)
public class QueryParamParameterResolver implements ParameterResolver {
    @SneakyThrows
    @Override
    public boolean resolved(RequestBuilder builder, Parameter parameter, Object value) {
        QueryParam annotationAnn = parameter.getAnnotation(QueryParam.class);
        UriComponentsBuilder uriComponentsBuilder = builder.getUriComponentsBuilder();
        if (annotationAnn == null) {
            return false;
        }
        String n = (String) AnnotationUtils.getValue(annotationAnn);
        if (n == null || "".equals(n)) {
            n = parameter.getName();
        }

        if (value instanceof Map) {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            ((Map<?, ?>) value).forEach((mK, mV) -> {
                if (mV instanceof Collection) {
                    List<String> stringV = ((Collection<?>) mV).stream()
                            .map(Object::toString)
                            .collect(Collectors.toList());
                    params.addAll(mK.toString(), stringV);
                } else {
                    if (mV instanceof Object[]) {
                        List<String> stringV = Stream.of((Object[]) mV)
                                .map(Object::toString)
                                .collect(Collectors.toList());
                        params.addAll(mK.toString(), stringV);
                    } else {
                        params.add(mK.toString(), mV.toString());
                    }
                }
            });
            uriComponentsBuilder.replaceQueryParams(params);
            return true;
        }

        Object v;
        Class<?> type = parameter.getType();
        if (BeanUtils.isSimpleValueType(type)) {
            v = value;
        } else if (Collection.class.isAssignableFrom(type)) {
            v = ((Collection<?>) value).toArray();
        } else {
            v = BeanUtil.beanToMap(value);
        }
        uriComponentsBuilder.queryParam(n, v);
        return true;
    }
}

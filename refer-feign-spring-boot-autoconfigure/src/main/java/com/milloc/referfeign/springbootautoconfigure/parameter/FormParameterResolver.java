package com.milloc.referfeign.springbootautoconfigure.parameter;

import com.milloc.referfeign.springbootautoconfigure.client.RequestBuilder;
import com.milloc.referfeign.springbootautoconfigure.util.BeanUtil;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Parameter;
import java.util.Map;

@Order
public class FormParameterResolver implements ParameterResolver {
    @SneakyThrows
    @SuppressWarnings("unchecked")
    @Override
    public boolean resolved(RequestBuilder builder, Parameter param, Object value) {
        if (HttpMethod.POST.equals(builder.getHttpMethod())) {
            MediaType contentType = builder.getHttpHeaders().getContentType();
            if (MediaType.MULTIPART_FORM_DATA.equals(contentType) || MediaType.APPLICATION_FORM_URLENCODED.equals(contentType)) {
                RequestBody annotation = param.getAnnotation(RequestBody.class);
                if (annotation != null) {
                    Map<?, ?> beanMap = null;
                    if (value instanceof Map) {
                        beanMap = (Map<?, ?>) value;
                    }
                    if (BeanUtils.isSimpleValueType(value.getClass())) {
                        beanMap = BeanUtil.beanToMap(value);
                    }
                    if (beanMap == null) {
                        return false;
                    }
                    MultiValueMap<Object, Object> params = new LinkedMultiValueMap<>();
                    beanMap.forEach(params::add);
                    builder.setBody(params);
                    return true;
                }

                MultiValueMap<Object, Object> body;
                if (builder.getBody() != null && builder.getBody() instanceof MultiValueMap) {
                    body = (MultiValueMap<Object, Object>) builder.getBody();
                } else {
                    body = new LinkedMultiValueMap<>();
                }

                String name = param.getName();
                if (value instanceof MultipartFile) {
                    MultipartFile multipartFile = (MultipartFile) value;
                    name = multipartFile.getName();
                    value = multipartFile.getResource();
                }

                body.add(name, value);
                builder.setBody(body);
                return true;
            }
        }
        return false;
    }
}

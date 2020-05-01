package com.milloc.referfeign.springbootautoconfigure.client;

import com.milloc.referfeign.springbootautoconfigure.parameter.ParameterResolver;
import com.milloc.referfeign.springbootautoconfigure.parameter.RequestBuilder;
import lombok.SneakyThrows;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.URI;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * @author gongdeming
 * @create 2019-09-05
 */
public class ReferInvocationHandler implements InvocationHandler {
    private final RestTemplate restTemplate;
    private final List<ParameterResolver> parameterResolvers;
    private final Class<?> clientType;

    public ReferInvocationHandler(RestTemplate restTemplate, List<ParameterResolver> parameterResolvers, Class<?> clientType) {
        this.restTemplate = restTemplate;
        this.parameterResolvers = parameterResolvers;
        this.clientType = clientType;
    }

    @Override
    @SneakyThrows
    public Object invoke(Object proxy, Method method, Object[] args) throws ReferClientResolveException {
        // 解析url
        Annotation[] annotations = method.getAnnotations();
        AnnotatedElement annotatedElement = AnnotatedElementUtils.forAnnotations(annotations);
        RequestMapping requestMappingAnn = AnnotatedElementUtils.findMergedAnnotation(annotatedElement, RequestMapping.class);
        requireNonNull(requestMappingAnn, "没有找到 RequestMapping");
        // path
        if (requestMappingAnn.path().length <= 0) {
            throw new ReferClientResolveException("没有找到 RequestMapping.path");
        }
        String path = requestMappingAnn.path()[0];
        // mediaType
        MediaType mediaType = requestMappingAnn.consumes().length <= 0 ? MediaType.TEXT_PLAIN :
                MediaType.valueOf(requestMappingAnn.consumes()[0]);

        // method
        HttpMethod httpMethod = requestMappingAnn.method().length <= 0 ?
                HttpMethod.GET : HttpMethod.valueOf(requestMappingAnn.method()[0].name());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(path);

        RequestBuilder requestBuilder = new RequestBuilder(uriComponentsBuilder, headers, httpMethod, null);
        // 调用resolver 解析param
        if (method.getParameterCount() > 0) {
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                boolean resolved = false;

                for (ParameterResolver parameterResolver : parameterResolvers) {
                    if (parameterResolver.resolved(requestBuilder, parameters[i], args[i])) {
                        resolved = true;
                        break;
                    }
                }
                if (!resolved) {
                    throw new ReferClientResolveException("无法解析参数 name=" + parameters[i].getName());
                }
            }
        }

        URI uri = requestBuilder.getUriComponentsBuilder().build().toUri();
        HttpEntity<?> httpEntity = new HttpEntity<>(requestBuilder.getBody(), requestBuilder.getHttpHeaders());
        Type returnType = method.getGenericReturnType();
        // 发送请求
        ResponseEntity<Object> responseEntity = restTemplate.exchange(uri, httpMethod, httpEntity, ParameterizedTypeReference.forType(returnType));
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            throw new ReferClientResolveException(String.format("返回结果错误 %s", responseEntity.toString()));
        }
    }
}

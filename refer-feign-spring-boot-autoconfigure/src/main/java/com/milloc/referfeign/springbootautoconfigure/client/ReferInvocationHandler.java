package com.milloc.referfeign.springbootautoconfigure.client;

import com.milloc.referfeign.springbootautoconfigure.parameter.ParameterResolver;
import lombok.SneakyThrows;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.URI;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * 实际方法调用
 *
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
        // 解析接口上的配置，生成默认的配置
        RequestBuilder requestBuilder = parseReferClient(clientType);

        // 解析当前方法的 RequestMapping
        Annotation[] annotations = method.getAnnotations();
        AnnotatedElement annotatedElement = AnnotatedElementUtils.forAnnotations(annotations);
        RequestMapping requestMappingAnn = AnnotatedElementUtils.findMergedAnnotation(annotatedElement, RequestMapping.class);
        if (requestMappingAnn == null) {
            throw new ReferClientResolveException("没有找到 RequestMapping");
        }
        resolveRequestMapping(requestMappingAnn, requestBuilder);

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
        // 检测请求是否合法
        requestBuilder.checkValidation();

        // 发送请求并解析返回参数
        URI uri = requestBuilder.getUriComponentsBuilder().build().toUri();
        HttpMethod httpMethod = requestBuilder.getHttpMethod();
        HttpEntity<?> httpEntity = new HttpEntity<>(requestBuilder.getBody(), requestBuilder.getHttpHeaders());

        ResponseEntity<Object> responseEntity;
        Class<?> returnType = method.getReturnType();
        if (ResponseEntity.class.isAssignableFrom(returnType) || HttpStatus.class.isAssignableFrom(returnType)) {
            // 解析 ResponseEntity HttpStatus 类型
            responseEntity = restTemplate.exchange(uri, httpMethod, httpEntity, Object.class);
            if (ResponseEntity.class.isAssignableFrom(returnType)) {
                return responseEntity;
            }
            return responseEntity.getStatusCode();
        } else {
            // 其他类型
            Type genericReturnType = method.getGenericReturnType();
            responseEntity = restTemplate.exchange(uri, httpMethod, httpEntity, ParameterizedTypeReference.forType(genericReturnType));
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity.getBody();
            } else {
                throw new ReferClientResolveException("返回结果错误 " + responseEntity.toString());
            }
        }
    }

    RequestBuilder parseReferClient(Class<?> clientType) {
        RequestBuilder clientRequestConfig = new RequestBuilder();
        Annotation[] annotations = clientType.getAnnotations();
        AnnotatedElement annotatedElement = AnnotatedElementUtils.forAnnotations(annotations);
        RequestMapping requestMappingAnn = AnnotatedElementUtils.findMergedAnnotation(annotatedElement, RequestMapping.class);
        resolveRequestMapping(requestMappingAnn, clientRequestConfig);
        return clientRequestConfig;
    }

    private void resolveRequestMapping(RequestMapping requestMappingAnn, RequestBuilder requestBuilder) {
        if (requestMappingAnn != null) {
            // uri
            if (requestMappingAnn.path().length > 0) {
                requestBuilder.uri(requestMappingAnn.path()[0]);
            }
            // method
            if (requestMappingAnn.method().length > 0) {
                requestBuilder.setHttpMethod(HttpMethod.resolve(requestMappingAnn.method()[0].name()));
            }
            // content-type
            if (requestMappingAnn.consumes().length > 0) {
                requestBuilder.mediaType(MediaType.valueOf(requestMappingAnn.consumes()[0]));
            }
            // header
            if (requestMappingAnn.headers().length > 0) {
                requestBuilder.httpHeaders(resolveHeadersFromString(requestMappingAnn.headers()));
            }
        }
    }

    private HttpHeaders resolveHeadersFromString(String[] headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        for (String header : headers) {
            int sep;
            if ((sep = header.indexOf("=")) > 0 && sep < header.length()) {
                String name = header.substring(0, sep);
                String value = header.substring(sep + 1);
                httpHeaders.add(name, value);
            }
        }
        return httpHeaders;
    }
}

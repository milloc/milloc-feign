package com.milloc.dbfeignspringbootautoconfigure.client;

import lombok.SneakyThrows;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

import static java.util.Objects.*;
import static java.util.stream.Collectors.toList;

/**
 * @author gongdeming
 * @create 2019-09-05
 */
public class DBInvocationHandler implements InvocationHandler {
    private final RestTemplate restTemplate;
    private final RequestMapping clientParam;

    DBInvocationHandler(RestTemplate restTemplate, Class clientInterface) {
        this.restTemplate = restTemplate;
        this.clientParam = AnnotationUtils.findAnnotation(clientInterface, RequestMapping.class);
    }

    @Override
    @SneakyThrows
    public Object invoke(Object proxy, Method method, Object[] args) {
        RequestMapping mapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
        requireNonNull(mapping);

        String path = mapping.path().length > 0 ? mapping.path()[0] : "";
        Parameter[] parameters = method.getParameters();
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        Map<String, Object> params = new HashMap<>();
        Object body = null;

        // 遍历参数，解码path，获取请求参数
        for (int i = 0; i < parameters.length; i++) {
            Parameter p = parameters[i];
            RequestBody rb = AnnotationUtils.findAnnotation(p, RequestBody.class);
            if (nonNull(rb)) {
                body = args[i];
            }
            PathVariable pv = AnnotationUtils.findAnnotation(p, PathVariable.class);
            if (nonNull(pv)) {
                String name = pv.name().isEmpty() ? p.getName() : pv.name();
                path = path.replaceAll("\\{" + name + "}", string(args[i]));
                continue;
            }
            RequestParam rp = AnnotationUtils.findAnnotation(p, RequestParam.class);
            if (nonNull(rp)) {
                String name = rp.name().isEmpty() ? p.getName() : rp.name();
                requestParams.add(name, string(args[i]));
                continue;
            }
            params.put(p.getName(), args[i]);
        }

        // 处理值，包括父级的值
        String uri = getUri(path, requestParams);
        HttpMethod httpMethod = getRequestMethod(mapping.method());
        List<MediaType> accepts = getAccepts(mapping.consumes());
        MediaType contentType = getContentType(mapping.produces());

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(accepts);
        headers.setContentType(contentType);
        HttpEntity<?> httpEntity = nonNull(body) ? new HttpEntity<>(body, headers) : new HttpEntity<>(params, headers);
        ParameterizedTypeReference<Object> type = ParameterizedTypeReference.forType(method.getGenericReturnType());

        // 请求
        ResponseEntity<Object> request = restTemplate.exchange(uri, httpMethod, httpEntity, type);
        // 处理返回结果
        // todo 将其暴露出去，可以自己定义
        if (!request.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException(String.format("错误的访问状态%s", request.getStatusCodeValue()));
        }
        return request.getBody();
    }

    private String getUri(String path, MultiValueMap<String, String> params) {
        if (clientParam != null && clientParam.path().length > 0) {
            return UriComponentsBuilder.fromUriString(clientParam.path()[0]).path(path).queryParams(params).toUriString();
        }
        if (path.isEmpty()) {
            throw new IllegalArgumentException("缺少 path");
        }
        return UriComponentsBuilder.fromUriString(path).queryParams(params).toUriString();
    }

    private HttpMethod getRequestMethod(RequestMethod[] requestMethods) {
        RequestMethod method;
        if (requestMethods.length > 0) {
            method = requestMethods[0];
        } else {
            method = nonNull(clientParam) && clientParam.method().length > 0 ? clientParam.method()[0] : RequestMethod.GET;
        }
        return HttpMethod.resolve(method.name());
    }

    private List<MediaType> getAccepts(String[] consumes) {
        String[] con;
        if (consumes.length > 0) {
            con = consumes;
        } else {
            con = nonNull(clientParam) && clientParam.consumes().length > 0 ? clientParam.consumes() : null;
        }
        return nonNull(con) ? Arrays.stream(con).map(MediaType::valueOf).collect(toList()) : Collections.singletonList(MediaType.ALL);
    }

    private MediaType getContentType(String[] produces) {
        String produce;
        if (produces.length > 0) {
            produce = produces[0];
        } else {
            produce = clientParam.produces().length > 0 ? clientParam.produces()[0] : null;
        }
        return nonNull(produce )? MediaType.valueOf(produce) : MediaType.APPLICATION_JSON_UTF8;
    }

    private String string(Object o) {
        return isNull(o) ? "" : String.valueOf(o);
    }
}

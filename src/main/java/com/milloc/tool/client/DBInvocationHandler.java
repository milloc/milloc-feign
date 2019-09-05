package com.milloc.tool.client;

import lombok.SneakyThrows;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

/**
 * @author gongdeming
 * @create 2019-09-05
 */
public class DBInvocationHandler implements InvocationHandler {
    private final RestTemplate restTemplate;

    public DBInvocationHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    @SneakyThrows
    public Object invoke(Object proxy, Method method, Object[] args) {
        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        if (mapping.path().length == 0) {
            throw new NullPointerException("path不能为空");
        }
        String path = mapping.path()[0];

        Parameter[] parameters = method.getParameters();
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < parameters.length; i++) {
            Parameter p = parameters[i];
            PathVariable pv = p.getAnnotation(PathVariable.class);
            if (pv != null) {
                String name = pv.name().isEmpty() ? p.getName() : pv.name();
                path = path.replaceAll("\\{" + name + "}", string(args[i]));
                continue;
            }
            RequestParam rp = p.getAnnotation(RequestParam.class);
            if (rp != null) {
                String name = rp.name().isEmpty() ? p.getName() : rp.name();
                requestParams.add(name, string(args[i]));
                continue;
            }
            params.put(p.getName(), args[i]);
        }

        String uri = UriComponentsBuilder.fromUriString(path).queryParams(requestParams).toUriString();
        RequestMethod requestMethod = mapping.method().length > 0 ? mapping.method()[0] : RequestMethod.GET;
        HttpMethod httpMethod = HttpMethod.resolve(requestMethod.name());
        HttpHeaders headers = new HttpHeaders();
        List<MediaType> accepts = mapping.consumes().length > 0 ?  Arrays.stream(mapping.consumes())
                .map(MediaType::valueOf)
                .collect(Collectors.toList()) : Collections.singletonList(MediaType.ALL);
        headers.setAccept(accepts);
        headers.setContentType(mapping.produces().length > 0 ? MediaType.valueOf(mapping.produces()[0]) :
                MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(params, headers);
        ParameterizedTypeReference<Object> type = ParameterizedTypeReference.forType(method.getGenericReturnType());

        ResponseEntity<Object> request = restTemplate.exchange(uri, httpMethod, httpEntity, type);
        if (!request.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException(String.format("错误的访问状态%s", request.getStatusCodeValue()));
        }
        return request.getBody();
    }

    private String string(Object o) {
        return isNull(o) ? "" : String.valueOf(o);
    }
}

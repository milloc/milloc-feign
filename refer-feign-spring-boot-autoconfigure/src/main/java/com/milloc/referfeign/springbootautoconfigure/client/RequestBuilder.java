package com.milloc.referfeign.springbootautoconfigure.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestBuilder {
    private UriComponentsBuilder uriComponentsBuilder;
    private HttpHeaders httpHeaders;
    private HttpMethod httpMethod;
    private Object body;

    void uri(String uri) {
        if (uriComponentsBuilder != null && uri.startsWith("/")) {
            uriComponentsBuilder.path(uri);
        } else {
            uriComponentsBuilder = UriComponentsBuilder.fromUriString(uri);
        }
    }

    void mediaType(MediaType mediaType) {
        if (httpHeaders == null) {
            httpHeaders = new HttpHeaders();
        }
        httpHeaders.setContentType(mediaType);
    }

    void httpHeaders(HttpHeaders httpHeaders) {
        if (this.httpHeaders == null) {
            this.httpHeaders = httpHeaders;
        } else {
            this.httpHeaders.addAll(httpHeaders);
        }
    }

    void checkValidation() {
        if (uriComponentsBuilder == null) {
            throw new ReferClientResolveException("不能解析到 RequestMapping.path");
        }
    }
}

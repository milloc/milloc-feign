package com.milloc.referfeign.springbootautoconfigure.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponentsBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestBuilder {
    private UriComponentsBuilder uriComponentsBuilder;
    private HttpHeaders httpHeaders;
    private HttpMethod httpMethod;
    private Object body;
}

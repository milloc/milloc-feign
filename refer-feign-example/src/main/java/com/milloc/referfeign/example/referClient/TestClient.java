package com.milloc.referfeign.example.referClient;

import com.milloc.referfeign.example.dto.TestDTO;
import com.milloc.referfeign.springbootautoconfigure.annotation.ReferClient;
import com.milloc.referfeign.springbootautoconfigure.annotation.QueryParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

@ReferClient
public interface TestClient {
    @PostMapping(path = "http://localhost/{a}/{b}")
    String post(@PathVariable("a") String a, @PathVariable("b") String b, @QueryParam("hello") String hello, @QueryParam String ccc);

    @GetMapping(path = "http://localhost/{a}/{b}")
    String get(@PathVariable("a") String a, @PathVariable("b") String b, @QueryParam("hello") String hello, @QueryParam String ccc);

    @PostMapping(value = "http://localhost/bcdd", consumes = MediaType.APPLICATION_JSON_VALUE)
    String json(@RequestBody TestDTO testDTO, @QueryParam("hello") String hello);

    @PostMapping(value = "http://localhost/bcdd", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String form(@RequestBody TestDTO testDTO, @QueryParam("hello") String hello);

    @PostMapping(value = "http://localhost/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String file(MultipartFile file);
}

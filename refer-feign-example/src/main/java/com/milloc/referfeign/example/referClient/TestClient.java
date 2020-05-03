package com.milloc.referfeign.example.referClient;

import com.milloc.referfeign.example.dto.TestDTO;
import com.milloc.referfeign.springbootautoconfigure.annotation.QueryParam;
import com.milloc.referfeign.springbootautoconfigure.annotation.ReferClient;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@ReferClient
@RequestMapping(path = "http://localhost/bb", headers = "X-Token=132312fdfdf3xxxeefef")
public interface TestClient {
    @PostMapping(path = "/{a}/{b}")
    String post(@PathVariable("a") String a, @PathVariable("b") String b, @QueryParam("hello") String hello, @QueryParam String ccc);

    @GetMapping(path = "http://localhost/{a}/{b}")
    String get(@PathVariable("a") String a, @PathVariable("b") String b, @QueryParam("hello") String hello, @QueryParam String ccc);

    @PostMapping(value = "/bcdd", consumes = MediaType.APPLICATION_JSON_VALUE)
    String json(@RequestBody TestDTO testDTO, @QueryParam("hello") String hello);

    @PostMapping(value = "/bcdd", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String form(@RequestBody TestDTO testDTO, @QueryParam("hello") String hello);

    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String file(MultipartFile file);
}

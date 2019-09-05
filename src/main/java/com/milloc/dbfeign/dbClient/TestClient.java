package com.milloc.dbfeign.dbClient;

import com.milloc.tool.annotation.DBClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@DBClient
public interface TestClient {
    @RequestMapping(path = "http://localhost:8000/{a}/{b}", method = {RequestMethod.GET})
    Map<String, Object> testGet(@PathVariable("a") String a, @PathVariable("b") String b, @RequestParam("hello") String hello, String ccc);

    @RequestMapping(path = "http://localhost:8000/{a}/{b}", method = {RequestMethod.POST})
    Map<String, Object> testPost(@PathVariable("a") String a, @PathVariable("b") String b, @RequestParam("hello") String hello, String ccc);
}

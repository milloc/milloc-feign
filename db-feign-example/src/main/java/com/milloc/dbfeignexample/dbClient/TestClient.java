package com.milloc.dbfeignexample.dbClient;

import com.milloc.dbfeignexample.dto.TestDTO;
import com.milloc.dbfeignspringbootautoconfigure.annotation.DBClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@DBClient
@RequestMapping(value = "http://localhost:8000", method = RequestMethod.POST)
public interface TestClient {
    @RequestMapping(path = "/{a}/{b}", method = {RequestMethod.GET})
    Map<String, Object> testGet(@PathVariable("a") String a, @PathVariable("b") String b, @RequestParam("hello") String hello, String ccc);

    @RequestMapping(path = "/{a}/{b}", method = {RequestMethod.POST})
    TestDTO testPost(@PathVariable("a") String a, @PathVariable("b") String b, @RequestParam("hello") String hello, String ccc);

    @RequestMapping("/bcdd")
    Map<String, Object> test(@RequestBody String abc, @RequestParam("hello") String hello);
}

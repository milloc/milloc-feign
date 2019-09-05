package com.milloc.dbfeign;

import com.milloc.dbfeign.dbClient.TestClient;
import com.milloc.tool.client.DBInvocationHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Proxy;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DbFeignApplicationTests {
    @Autowired
    private RestTemplate restTemplate;
    @Test
    public void contextLoads() {
        TestClient testClient = (TestClient) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{TestClient.class}, new DBInvocationHandler(restTemplate));
        Map<String, Object> map = testClient.testPost("aaa", "bbb", "hello", "cccc");
        System.out.println("map = " + map);
    }

}

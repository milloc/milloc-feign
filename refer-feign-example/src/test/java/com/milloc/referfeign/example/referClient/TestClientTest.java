package com.milloc.referfeign.example.referClient;

import com.milloc.referfeign.example.ReferFeignExampleApplication;
import com.milloc.referfeign.example.dto.TestDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest(classes = ReferFeignExampleApplication.class)
@RunWith(SpringRunner.class)
public class TestClientTest {
    @Resource
    private TestClient testClient;

    @Test
    public void get() {
        System.out.println(testClient.get("a", "b", "hello", "dfdfd"));
    }

    @Test
    public void post() {
        System.out.println(testClient.post("a", "b", "hello", "dfdfd"));
    }

    @Test
    public void json() {
        System.out.println(testClient.json(new TestDTO("dfdf", null, null), "b"));
    }

    @Test
    public void form() {
        System.out.println(testClient.form(new TestDTO("fff", null, null), "b"));
    }
}
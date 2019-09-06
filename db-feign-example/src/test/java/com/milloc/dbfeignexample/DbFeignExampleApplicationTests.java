package com.milloc.dbfeignexample;

import com.milloc.dbfeignexample.dbClient.TestClient;
import com.milloc.dbfeignexample.dto.TestDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DbFeignExampleApplicationTests {
    @Autowired
    private TestClient testClient;
    @Test
    public void contextLoads() {
        TestDTO map = testClient.testPost("aa", "bb", "cc", "ddd");
        System.out.println("map = " + map);
    }
}

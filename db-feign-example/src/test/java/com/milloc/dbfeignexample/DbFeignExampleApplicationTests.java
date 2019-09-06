package com.milloc.dbfeignexample;

import com.milloc.dbfeignexample.dbClient.TestClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DbFeignExampleApplicationTests {
    @Autowired
    private TestClient testClient;
    @Test
    public void contextLoads() {
        Object o = testClient.test("ddfdfd", "dddd");
        System.out.println("o = " + o);
    }
}

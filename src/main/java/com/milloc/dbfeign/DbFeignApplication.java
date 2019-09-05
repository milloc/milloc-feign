package com.milloc.dbfeign;

import com.milloc.tool.DBFeignConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = DBFeignConfigurer.class)
public class DbFeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(DbFeignApplication.class, args);
    }

}

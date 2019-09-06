package com.milloc.dbfeignexample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author gongdeming
 * @create 2019-09-05
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}

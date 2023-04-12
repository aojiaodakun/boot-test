package com.hzk.config;

import com.hzk.boot.beans.Student;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 测试autoconfig机制
 */
@Configuration
//@ConditionalOnProperty("mservice.assembly.type")
public class AppConfig {

    @Bean
    public Student student(){
        return new Student();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}

package com.hzk.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ApplicationMain {

    private static final String DISCOVERY_TYPE = "discovery.type";

    static {
        // AutoConfigurationImportFilter开关
        System.setProperty("mservice.boot.autoconfig.filter.enable", "true");
        System.setProperty("KAutoConfigSpringFactoryFiles.config", "my-spring-spring.factories");




        System.setProperty(DISCOVERY_TYPE, "zookeeper");
        System.setProperty(DISCOVERY_TYPE, "nacos");
//        System.setProperty(DISCOVERY_TYPE, "eureka");


    }

    public static void main(String[] args) {
        String discoveryType = System.getProperty(DISCOVERY_TYPE);
        if (discoveryType.equals("zookeeper")) {
            // zookeeper注册中心
            System.setProperty("spring.cloud.zookeeper.enabled", "true");
            System.setProperty("spring.cloud.zookeeper.discovery.enabled", "true");
            System.setProperty("spring.cloud.nacos.discovery.enabled", "false");
            System.setProperty("eureka.client.enabled", "false");
        } else if(discoveryType.equals("nacos")){
            // nacos注册中心
            System.setProperty("spring.cloud.nacos.discovery.enabled", "true");
            System.setProperty("spring.cloud.zookeeper.enabled", "false");
            System.setProperty("spring.cloud.zookeeper.discovery.enabled", "false");
            System.setProperty("eureka.client.enabled", "false");
        } else if(discoveryType.equals("eureka")){
            // eureka注册中心
            System.setProperty("spring.cloud.nacos.discovery.enabled", "false");
            System.setProperty("spring.cloud.zookeeper.enabled", "false");
            System.setProperty("spring.cloud.zookeeper.discovery.enabled", "false");
            System.setProperty("eureka.client.enabled", "true");
        }
        SpringApplication.run(ApplicationMain.class, args);

    }

}

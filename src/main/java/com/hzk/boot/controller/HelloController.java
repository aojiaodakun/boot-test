package com.hzk.boot.controller;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.hzk.boot.mservice.IDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Controller
public class HelloController {

    private ConfigService configService;

    @Autowired(required = false)
    private IDemoService demoService;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * localhost:8083/nacos/test1?dataId=prop.yaml&group=common
     */
    @RequestMapping("/nacos/test1")
    @ResponseBody
    public String nacosTest1(@RequestParam("dataId") String dataId, @RequestParam("group") String group) throws NacosException {
        if (configService == null) {
            Properties properties = new Properties();
            String serverAddr = "172.20.158.138:8848";
            properties.put("serverAddr", serverAddr);
            properties.put("namespace", "sit");
            properties.put(PropertyKeyConst.USERNAME, "nacos");
            properties.put(PropertyKeyConst.PASSWORD, "nacos12");
            configService = NacosFactory.createConfigService(properties);
        }
        String config = configService.getConfig(dataId, group, 5000);
        return config;
    }

    @RequestMapping("/mserviceProvider")
    @ResponseBody
    public String mserviceProvider(){
        return "hello,i am boot-test";
    }

    @RequestMapping("/mserviceConsumer")
    @ResponseBody
    public String mserviceConsumer() throws Exception{
//        ResponseEntity<String> resultResponse = restTemplate.exchange("http://localhost:8084/mserviceProvider", HttpMethod.GET, null, String.class);
//        String body = resultResponse.getBody();

        String hello = "hello";
        hello = demoService.hello();
        System.out.println(hello);
        return hello;
    }

    @RequestMapping("/hello")
    @ResponseBody
    public String hello(@RequestParam(value = "name", required = false) String name){
        if (name == null) {
            return "hello,i am boot-test";
        }
        return "hello:" + name;
    }

    /**
     * http://localhost:8081/oom
     * @return
     */
    @RequestMapping("/oom")
    @ResponseBody
    public String oom(){
        List<Byte[]> list = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            list.add(new Byte[1024 * 1024 * 5]);
        }
        return "oom";
    }

    @RequestMapping("/exit")
    @ResponseBody
    public String exit(){
        System.exit(1);
        return "exiting";
    }

}

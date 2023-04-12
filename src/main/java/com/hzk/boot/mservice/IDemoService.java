package com.hzk.boot.mservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient(value = "kdcloud-test")
public interface IDemoService {

    @GetMapping(value = "mserviceProvider")
    String hello();

}

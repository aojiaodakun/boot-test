package com.hzk.boot.listener;

import com.hzk.boot.hook.MyShutdownHook;
import com.hzk.boot.mservice.IDemoService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRereshListener implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext.getBean("student");
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Runtime.getRuntime().addShutdownHook(new MyShutdownHook());
    }


}

package com.hzk.boot.beans;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

//@Component
public class HzkFactoryBean implements FactoryBean<Student> {
    @Override
    public Student getObject() throws Exception {
        return new Student();
    }

    @Override
    public Class<?> getObjectType() {
        return Student.class;
    }
}

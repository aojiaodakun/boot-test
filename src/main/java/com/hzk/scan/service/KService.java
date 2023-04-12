package com.hzk.scan.service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
public @interface KService {
    String group();
    String name();
    /**
     * use "*" special All appIds
     * @return
     */
    String[] appIds();

    String description() default "javaobj";

    String[] transprotocalType() default "rpc";

    String[] dataCodec();

}
package com.hzk.boot.autoconfigure;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class MyAutoConfigurationImportFilter implements AutoConfigurationImportFilter, EnvironmentAware {
    // filter开关参数
    private static final String MSERVICE_BOOT_AUTOCONFIG_FILTER_ENABLE = "mservice.boot.autoconfig.filter.enable";
    // 二开的自动配置类白名单参数
    private static final String MSERVICE_AUTOCONFIG_CONFIG = "KAutoConfigSpringFactoryFiles.config";
    // filter开关，默认开
    private static AtomicBoolean isFilterEnable = new AtomicBoolean(false);
    // 自动配置类白名单
    private static Set<String> ALL_AUTOCONFIG_FULLCLASSNAME_SET = new HashSet<>();
    // 供AutoConfigurationImportSelector#getAutoConfigurationEntry第一次调用使用，自动配置类的类头@Import不适用
    private static volatile boolean isMatchMethodInvoke;


    @Override
    public void setEnvironment(Environment environment) {
        isFilterEnable.compareAndSet(false, Boolean.parseBoolean(environment.getProperty(MSERVICE_BOOT_AUTOCONFIG_FILTER_ENABLE, "true")));
        if (isFilterEnable.get()) {
            init(environment);
        }
    }

    private void init(Environment environment){
        // 标品自动配置类白名单
        loadAutoConfigClasses("META-INF/bos-autoconfig-spring.factories");
        loadAutoConfigClasses("META-INF/trd-autoconfig-spring.factories");
        // 二开自动配置类白名单
        String extSpringFactoryFiles = environment.getProperty(MSERVICE_AUTOCONFIG_CONFIG);
        if (StringUtils.isNotEmpty(extSpringFactoryFiles)) {
            String[] extSpringFactoryFileArray = extSpringFactoryFiles.split(",");
            for (String tempFile : extSpringFactoryFileArray) {
                loadAutoConfigClasses(tempFile);
            }
        }
    }

    private void loadAutoConfigClasses(String fileName){
        try (InputStream inStream = MyAutoConfigurationImportFilter.class.getClassLoader().getResourceAsStream(fileName)) {
            String[] autoConfigClasses = IOUtils.toString(inStream).split("\r\n");
            for(String tempClassName : autoConfigClasses) {
                ALL_AUTOCONFIG_FULLCLASSNAME_SET.add(tempClassName.trim());
            }
        } catch (IOException e) {

        }
    }


    @Override
    public boolean[] match(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata) {
        boolean[] resultBooleans = new boolean[autoConfigurationClasses.length];
        if (!isFilterEnable.get()) {
            Arrays.fill(resultBooleans, true);
            return resultBooleans;
        }
        if (isMatchMethodInvoke) {
            Arrays.fill(resultBooleans, true);
            return resultBooleans;
        }
        for (int i = 0; i < autoConfigurationClasses.length; i++) {
            String tempClassName = autoConfigurationClasses[i];
            if (ALL_AUTOCONFIG_FULLCLASSNAME_SET.contains(tempClassName)) {
                resultBooleans[i] = true;
            }
        }
        isMatchMethodInvoke = true;
        return resultBooleans;
    }

}

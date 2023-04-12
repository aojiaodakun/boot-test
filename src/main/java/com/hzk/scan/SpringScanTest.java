package com.hzk.scan;

import com.hzk.scan.service.KService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.ClassUtils;

import java.io.IOException;

public class SpringScanTest {

    static String basePackage = "com.hzk.scan.*";

    private static Environment environment = new StandardEnvironment();

    private static final String resourcePattern = "**/*.class";

    public static void main(String[] args) throws Exception{

        invokeSpring();

    }


    static void invokeSpring() throws IOException {
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                resolveBasePackage(basePackage) + '/' + resourcePattern;
        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);

        CachingMetadataReaderFactory cachingMetadataReaderFactory = new CachingMetadataReaderFactory();
        for (Resource resource : resources) {
            if (resource.isReadable()) {
                MetadataReader metadataReader = cachingMetadataReaderFactory.getMetadataReader(resource);
                ClassMetadata classMetadata = metadataReader.getClassMetadata();
                String className = classMetadata.getClassName();
                // 跳过内部类
                if (className.contains("$")) {
                    continue;
                }

                AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
                boolean isFlag = annotationMetadata.isAnnotated(KService.class.getName());
                System.out.println(isFlag);



            }
        }

    }

    private static String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(getEnvironment().resolveRequiredPlaceholders(basePackage));
    }

    private static Environment getEnvironment() {
        return environment;
    }

}

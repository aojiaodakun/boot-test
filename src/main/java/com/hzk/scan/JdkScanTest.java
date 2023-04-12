package com.hzk.scan;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.tree.AnnotationNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;

public class JdkScanTest {

    public static void main(String[] args) throws Exception{
        String myPackage = "com.hzk.scan";
        String resolveBasePackage = myPackage.replace(".", "/");
        if (!resolveBasePackage.endsWith("/")) {
            resolveBasePackage = resolveBasePackage + "/";
        }

        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolveBasePackage;
        URL url = KDScanTest.class.getClassLoader().getResource(resolveBasePackage);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();


        Collection<File> fileList = FileUtils.listFiles(new File(url.getPath()), new String[]{"class"}, true);
        for (File tempFile : fileList) {
            String name = tempFile.getName();
            System.out.println(name);
            // 跳过内部类
            if (name.contains("$")) {
                continue;
            }
            FileInputStream inputStream = new FileInputStream(tempFile);
            ClassReader classReader = new ClassReader(inputStream);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);
            // 判断是否为接口或注解
            List<MethodNode> methodNodeList = classNode.methods;
            boolean isInterface = true;
            for(MethodNode tempMethodNode : methodNodeList) {
                String tempMethodName = tempMethodNode.name;
                if (tempMethodName.equals("<init>")) {
                    isInterface = false;
                    break;
                }
            }
            if (isInterface) {
                inputStream.close();
                continue;
            }
            List<AnnotationNode> annotationNodeList = classNode.visibleAnnotations;
            if (annotationNodeList != null && annotationNodeList.size() > 0) {
                for(AnnotationNode tempNode : annotationNodeList) {
                    String nativeAnnotation = tempNode.desc.replaceAll("/", ".");
                    String annotationName = nativeAnnotation.substring(1, nativeAnnotation.length() - 1);
                    System.out.println(annotationName);

                }
            }
            inputStream.close();

        }
    }


}

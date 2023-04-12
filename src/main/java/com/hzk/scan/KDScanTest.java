package com.hzk.scan;

import org.apache.commons.io.FileUtils;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.ModuleVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.RecordComponentVisitor;
import org.objectweb.asm.TypePath;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class KDScanTest {

    static String basePackage = "com.hzk.scan.*";

    private static final String resourcePattern = "**/*.class";

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
            String className = classReader.getClassName();
            className = className.replace("/", ".");
            MyClassVisitor myClassVisitor = new MyClassVisitor(Opcodes.ASM9, className);
            classReader.accept(myClassVisitor, ClassReader.SKIP_DEBUG);

            System.out.println(classReader);

            inputStream.close();

        }

    }

}
class MyClassVisitor extends ClassVisitor {

    private final String className;
    // 具体类，非接口非注解
    private boolean isConcrete = false;
    // 是否为微服务接口
    private boolean isMService = false;

    private Set<String> annotationNameSet = new HashSet<>();

    public MyClassVisitor(int api, String className) {
        super(api);
        this.className = className;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        String nativeAnnotation = descriptor.replaceAll("/", ".");
        String annotationName = nativeAnnotation.substring(1, nativeAnnotation.length() - 1);
        annotationNameSet.add(annotationName);
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (name.equals("<init>")) {
            isConcrete = true;
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    @Override
    public void visitEnd() {
        if (isConcrete) {
            // 根据类头注解判断是否为微服务接口
        }
        super.visitEnd();
    }

    @Override
    public String toString() {
        return className;
    }
}
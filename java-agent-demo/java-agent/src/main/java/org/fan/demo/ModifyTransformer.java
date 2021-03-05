package org.fan.demo;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * @version 1.0
 * @author: Fan
 * @date 2021/3/5 10:45
 */
public class ModifyTransformer implements ClassFileTransformer {

  @Override
  public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
      ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
    if ("org/fan/demo/Main".equals(className)) {
      try {
        System.out.println("重构Main对象");
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.get("org.fan.demo.Main");
        CtMethod main = ctClass.getDeclaredMethod("main");
        main.insertBefore("log.info(\"[Main.main] start this is generate by javassist\");");
        main.insertAfter("log.info(\"[Main.main] end this is generate by javassist\");");
        byte[] bytes = ctClass.toBytecode();
        ctClass.detach();
        return bytes;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    // 返回null则不会修改
    return null;
  }
}

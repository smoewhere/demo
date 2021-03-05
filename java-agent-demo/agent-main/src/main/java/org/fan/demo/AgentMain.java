package org.fan.demo;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

/**
 * @version 1.0
 * @author: Fan
 * @date 2021/3/1 09:59
 */
public class AgentMain {

  public static void agentmain(String agentArgs, Instrumentation instrumentation) {
    instrumentation.addTransformer(new DefineTransformer(), true);
    try {
      Class<?> aClass = Class.forName("org.fan.demo.Main");
      instrumentation.retransformClasses(aClass);
    } catch (ClassNotFoundException | UnmodifiableClassException e) {
      e.printStackTrace();
    }
  }

  static class DefineTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
      System.out.println("premain load Class:" + className);
      if ("org/fan/demo/Main".equals(className)) {
        try {
          System.out.println("重构Main对象");
          ClassPool classPool = ClassPool.getDefault();
          CtClass ctClass = classPool.get("org.fan.demo.Main");
          CtMethod main = ctClass.getDeclaredMethod("getInt");
          String body = "return 50;";
          main.setBody(body);
          byte[] bytes = ctClass.toBytecode();
          ctClass.detach();
          System.out.println("重构Main对象结束");
          return bytes;
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      // 返回null则不会修改
      return null;
    }
  }

}

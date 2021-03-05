package org.fan.demo;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version 1.0
 * @author: Fan
 * @date 2021/3/1 09:59
 */
public class PreMainTraceAgent {

  private static final Logger log = LoggerFactory.getLogger(PreMainTraceAgent.class);

  public static void premain(String agentArgs, Instrumentation inst) {
    System.out.println("agentArgs : " + agentArgs);
    inst.addTransformer(new DefineTransformer(), true);
    inst.addTransformer(new ModifyTransformer(), true);
  }

  /**
   * 简单的打印加载的类
   */
  static class DefineTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
        ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
      System.out.println("premain load Class:" + className);
      return classfileBuffer;
    }
  }

}

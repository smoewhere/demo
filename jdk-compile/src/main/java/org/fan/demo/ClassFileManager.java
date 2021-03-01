package org.fan.demo;

import java.io.IOException;
import java.security.SecureClassLoader;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

/**
 * @version 1.0
 * @author: Fan
 * @date 2021/3/1 13:05
 */
public class ClassFileManager extends ForwardingJavaFileManager {

  /**
   * 存储编译后的代码数据
   */
  private JavaTargetFileObject classJavaFileObject;

  protected ClassFileManager(JavaFileManager fileManager) {
    super(fileManager);
  }

  /**
   * 编译后加载类
   * <p>
   * 返回一个匿名的SecureClassLoader:
   * 加载由JavaCompiler编译后，保存在ClassJavaFileObject中的byte数组。
   */
  @Override
  public ClassLoader getClassLoader(Location location) {
    return new SecureClassLoader() {
      @Override
      protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = classJavaFileObject.getBytes();
        return super.defineClass(name, bytes, 0, bytes.length);
      }
    };
  }

  /**
   * 给编译器提供JavaClassObject，编译器会将编译结果写进去
   */
  @Override
  public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling)
      throws IOException {
    this.classJavaFileObject = new JavaTargetFileObject(className, kind);
    return this.classJavaFileObject;
  }

}

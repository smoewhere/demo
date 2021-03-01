package org.fan.demo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import javax.tools.SimpleJavaFileObject;

/**
 * @version 1.0
 * @author: Fan
 * @date 2021/3/1 13:03
 */
public class JavaTargetFileObject extends SimpleJavaFileObject {

  /**
   * Compiler编译后的byte数据会存在这个ByteArrayOutputStream对象中，
   * 后面可以取出，加载到JVM中。
   */
  private ByteArrayOutputStream byteArrayOutputStream;

  public JavaTargetFileObject(String className, Kind kind) {
    super(URI.create("string:///" + className.replaceAll("\\.", "/") + kind.extension), kind);
    this.byteArrayOutputStream = new ByteArrayOutputStream();
  }

  /**
   * 覆盖父类SimpleJavaFileObject的方法。
   * 该方法提供给编译器结果输出的OutputStream。
   * <p>
   * 编译器完成编译后，会将编译结果输出到该 OutputStream 中，我们随后需要使用它获取编译结果
   *
   * @return
   * @throws IOException
   */
  @Override
  public OutputStream openOutputStream() throws IOException {
    return this.byteArrayOutputStream;
  }

  /**
   * FileManager会使用该方法获取编译后的byte，然后将类加载到JVM
   */
  public byte[] getBytes() {
    return this.byteArrayOutputStream.toByteArray();
  }
}

package org.fan.demo;

import java.io.IOException;
import java.net.URI;
import javax.tools.SimpleJavaFileObject;

/**
 * @version 1.0
 * @author: Fan
 * @date 2021/3/1 13:04
 */
public class JavaSourceFileObject extends SimpleJavaFileObject {

  //表示java源代码
  private CharSequence content;

  protected JavaSourceFileObject(String className, String content) {
    super(URI.create("string:///" + className.replaceAll("\\.", "/") + Kind.SOURCE.extension), Kind.SOURCE);
    this.content = content;
  }

  /**
   * 获取需要编译的源代码
   *
   * @param ignoreEncodingErrors
   * @return
   * @throws IOException
   */
  @Override
  public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
    return content;
  }
}

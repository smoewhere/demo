package org.fan.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 利用jdk自带的JavaCompile API，把java文件编译成class
 *
 * @version 1.0
 * @author: Fan
 * @date 2021/3/1 10:33
 */
public class CompileFile {

  private static final Logger log = LoggerFactory.getLogger(CompileFile.class);

  public static void main(String[] args) {
    String className = "GenerateTest";
    String fileName = "GenerateTest" + ".java";
    String builder = "package org.fan.demo.generate;\n"
        + "\n"
        + "public class " + className + " {\n"
        + "  public static void main(String[] args) {\n"
        + "    System.out.println(\"Hello world!\");\n"
        + "  }\n"
        + "}";
    log.info("generate code:\n{}", builder);
    URL resource = Thread.currentThread().getContextClassLoader().getResource("");
    String path = Optional.ofNullable(resource).map(url -> {
      try {
        URI uri = url.toURI();
        return Paths.get(uri).toString();
      } catch (URISyntaxException e) {
        return null;
      }
    }).orElseThrow(() -> new RuntimeException("res is null"));
    log.info("[CompileFile.main] path is {}", path);
    Path fullPath = Paths.get(path, fileName);
    log.info("[CompileFile.main] full path is {}", fullPath.toString());
    File file = fullPath.toFile();
    if (file.exists()) {
      file.delete();
    }
    try (FileOutputStream outputStream = new FileOutputStream(file);
        FileChannel channel = outputStream.getChannel()) {
      ByteBuffer byteBuffer = ByteBuffer.wrap(builder.getBytes());
      channel.write(byteBuffer);
      log.info("[CompileFile.main] write file success");
    } catch (IOException e) {
      log.error("[CompileFile.main]", e);
    }
    JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
    try {
      int result = javaCompiler.run(null, null, null, fullPath.toString());
      log.info("[CompileFile.main] compile result code is {}", result);
    } catch (Exception e) {
      log.error("[CompileFile.main]", e);
    }
  }
}

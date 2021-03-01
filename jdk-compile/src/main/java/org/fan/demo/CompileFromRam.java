package org.fan.demo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 从内存中调用JavaCompile API编译并调用
 *
 * @version 1.0
 * @author: Fan
 * @date 2021/3/1 11:02
 */
public class CompileFromRam {

  private static final Logger log = LoggerFactory.getLogger(CompileFromRam.class);


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
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();
    StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnosticCollector, null, null);
    JavaFileManager manager = new ClassFileManager(fileManager);

    JavaFileObject stringObject = new JavaSourceFileObject("org.fan.demo.generate." + className, builder);
    Iterable<? extends JavaFileObject> classes = Arrays.asList(stringObject);
    JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, null, null, classes);
    Boolean result = task.call();
    if (!result) {
      log.error("[CompileFromRam.main] error");
      return;
    }
    try {
      Class<?> loadClass = manager.getClassLoader(null).loadClass("org.fan.demo.generate." + className);
      //Class<?> aClass = Class.forName("org.fan.demo.generate." + className);
      Constructor<?> constructor = loadClass.getConstructor();
      Object o = constructor.newInstance();
      Method main = loadClass.getMethod("main", String[].class);
      main.invoke(o, new Object[] { new String[] {} });
    } catch (Exception e) {
      log.error("[CompileFromRam.main]", e);
    }
  }

  private static class StringObject extends SimpleJavaFileObject {

    private String content = null;

    protected StringObject(String className, String contents) {
      super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
      this.content = contents;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
      return content;
    }
  }
}

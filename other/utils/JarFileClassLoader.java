package org.fan.classloader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.9.23 13:18
 */
public class JarFileClassLoader extends ClassLoader {

  private final List<JarFile> jarFileList = new ArrayList<>();

  private volatile boolean isLoaded = false;

  private final HashMap<String, Class<?>> loadedClass = new HashMap<>(16);

  private final HashMap<String, JarInfo> jarInfoMap = new HashMap<>();


  public JarFileClassLoader(JarFile... jarFiles) {
    if (jarFiles != null && jarFiles.length > 0) {
      this.jarFileList.addAll(Arrays.asList(jarFiles));
    }
  }

  private Class<?> findMapClass(String name) {
    return loadedClass.get(name);
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    Class<?> aClass = findMapClass(name);
    if (aClass != null) {
      return aClass;
    }
    if (!isLoaded) {
      unpackJar();
    }
    JarInfo jarInfo = jarInfoMap.get(name);
    if (jarInfo == null) {
      throw new ClassNotFoundException();
    }
    aClass = defineClass(name, jarInfo.jarBytes, 0, jarInfo.jarBytes.length);
    if (aClass == null) {
      throw new ClassNotFoundException();
    }
    loadedClass.put(name, aClass);
    return aClass;
  }

  private void unpackJar() {
    for (JarFile jarFile : jarFileList) {
      Enumeration<JarEntry> entries = jarFile.entries();
      while (entries.hasMoreElements()) {
        JarEntry entry = entries.nextElement();
        if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
          try (InputStream inputStream = jarFile.getInputStream(entry)) {
            byte[] bytes = new byte[1024];
            int len = 0;
            ByteArrayOutputStream stream = new ByteArrayOutputStream(1024);
            while ((len = inputStream.read(bytes)) > 0) {
              stream.write(bytes, 0, len);
            }
            JarInfo jarInfo = new JarInfo();
            jarInfo.setJarBytes(stream.toByteArray());
            jarInfo.setJarClassName(getClassName(entry.getName()));
            jarInfoMap.put(jarInfo.getJarClassName(), jarInfo);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
    isLoaded = true;
  }

  private String getClassName(String name) {
    if (name == null || name.length() == 0) {
      return name;
    }
    String s = name.replaceAll("/", "\\.");
    return s.substring(0, s.length() - 6);
  }

  static class JarInfo {

    private byte[] jarBytes;
    private String jarClassName;

    public byte[] getJarBytes() {
      return jarBytes;
    }

    public void setJarBytes(byte[] jarBytes) {
      this.jarBytes = jarBytes;
    }

    public String getJarClassName() {
      return jarClassName;
    }

    public void setJarClassName(String jarClassName) {
      this.jarClassName = jarClassName;
    }
  }


}

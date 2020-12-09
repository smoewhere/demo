package org.fan.demo.hotload.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;
import org.fan.demo.hotload.handler.Handler;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.12.9 11:23
 */
@RestController
public class HotLoadController {

  public static final Map<String, Handler> classMap = new ConcurrentHashMap<>(16);

  public static final ObjectMapper mapper = new ObjectMapper();

  private static final String LIB_PATH = "F:\\gitLocal\\allLocal\\cloudpark\\lib";

  public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/alarm.factories";

  private static final Set<URLClassLoader> classLoaders = new CopyOnWriteArraySet<>();

  static {
    new Thread(() -> {
      while (true) {
        for (Handler handler : classMap.values()) {
          handler.say();
        }
        try {
          TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }


  @RequestMapping(path = "/getClassList")
  public String getClassList() {
    String result = null;
    try {
      result = mapper.writeValueAsString(classMap);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      result = e.getMessage();
    }
    return result;
  }

  @RequestMapping(path = "/refresh")
  public String refresh() {
    File file = new File(LIB_PATH);
    if (!file.exists()) {
      return LIB_PATH + " not exists";
    }
    List<File> allFile = getAllFile(file);
    ArrayList<URL> urls = new ArrayList<>(16);
    for (File file1 : allFile) {
      URL url = null;
      try {
        url = file1.toURI().toURL();
      } catch (MalformedURLException e) {
        e.printStackTrace();
        continue;
      }
      urls.add(url);
    }
    if (urls.size() == 0) {
      return "0";
    }
    URL[] urlArray = new URL[urls.size()];
    for (int i = 0; i < urls.size(); i++) {
      urlArray[i] = urls.get(i);
    }
    URLClassLoader classLoader = new URLClassLoader(urlArray, getClass().getClassLoader());
    Map<String, List<String>> result = new HashMap<>();
    try {
      Enumeration<URL> resources = classLoader.getResources(FACTORIES_RESOURCE_LOCATION);
      while (resources.hasMoreElements()) {
        URL url = resources.nextElement();
        UrlResource resource = new UrlResource(url);
        Properties properties = PropertiesLoaderUtils.loadProperties(resource);
        for (Map.Entry<?, ?> entry : properties.entrySet()) {
          String factoryTypeName = ((String) entry.getKey()).trim();
          String[] factoryImplementationNames =
              StringUtils.commaDelimitedListToStringArray((String) entry.getValue());
          for (String factoryImplementationName : factoryImplementationNames) {
            result.computeIfAbsent(factoryTypeName, key -> new ArrayList<>())
                .add(factoryImplementationName.trim());
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      return "0";
    }
    if (result.size() == 0) {
      return "";
    }
    List<String> alarmClassList = result.get("org.fan.demo.hotload.handler");
    Set<String> loadedClassNames = new HashSet<>();
    for (String clazzName : alarmClassList) {
      try {
        Class<?> loadClass = classLoader.loadClass(clazzName);
        Constructor<?> constructor = loadClass.getConstructor();
        Handler o = (Handler) constructor.newInstance();
        classMap.putIfAbsent(o.getClass().getSimpleName(), o);
        classMap.computeIfPresent(o.getClass().getSimpleName(), (key, oldValue) -> o);
        loadedClassNames.add(o.getClass().getSimpleName());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    for (Entry<String, Handler> entry : classMap.entrySet()) {
      if (!loadedClassNames.contains(entry.getKey())) {
        classMap.remove(entry.getKey(), entry.getValue());
      }
    }
    Iterator<URLClassLoader> loaderIterator = classLoaders.iterator();
    classLoaders.add(classLoader);
    return getString(loaderIterator);
  }

  private List<File> getAllFile(File root) {
    ArrayList<File> resultList = new ArrayList<>();
    if (root.isDirectory()) {
      File[] files = root.listFiles();
      if (files == null) {
        return resultList;
      }
      for (File file : files) {
        List<File> allFiles = getAllFile(file);
        resultList.addAll(allFiles);
      }
    } else {
      resultList.add(root);
    }
    return resultList;
  }

  @RequestMapping(path = "/clear")
  public String unLoadAll() {
    classMap.clear();
    Iterator<URLClassLoader> iterator = classLoaders.iterator();
    return getString(iterator);
  }

  private String getString(Iterator<URLClassLoader> iterator) {
    while (iterator.hasNext()) {
      URLClassLoader loader = iterator.next();
      try {
        loader.close();
        classLoaders.remove(loader);
        loader = null;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return "success";
  }


}

package org.fan.demo;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.8.17 13:23
 */
public class GuavaApp {

  public static void main(String[] args) {
    LoadingCache<String, String> cache = CacheBuilder.newBuilder().maximumSize(2).weakKeys().softValues()
        .refreshAfterWrite(120, TimeUnit.SECONDS).expireAfterWrite(20, TimeUnit.MINUTES)
        .removalListener((notification) -> {
          System.out.printf("remove key:%s,value is %s,caused by%s\n", notification.getKey(), notification.getValue(),
              notification.getCause().toString());
        }).build(
            new CacheLoader<String, String>() {
              @Override
              public String load(String key) throws Exception {
                System.out.println("first load");
                return key + ": key";
              }
            });
    try {
      String s = cache.get("1");
      cache.put("key2", "value2");
      cache.put("key3", "value3");
      cache.put("key4", "value4");
      cache.put("key5", "value5");
      System.out.println(cache.get("key2"));
      System.out.println(cache.get("1"));
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
  }

}

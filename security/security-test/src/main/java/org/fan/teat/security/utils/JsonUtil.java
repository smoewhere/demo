package org.fan.teat.security.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.10.21 18:32
 */
public class JsonUtil {

  private static final ObjectMapper mapper = new ObjectMapper();

  static {
    // 序列化的时候忽略null值
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    // 反序列化的时候忽略不存在的字段
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  private JsonUtil() {
  }

  /**
   * 对象转为json字符串
   *
   * @param value 对象
   * @return 字符串
   */
  public static String toJsonStr(Object value) {
    String str = null;
    try {
      str = mapper.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    return str;
  }

  /**
   * 对象转为字节数组
   *
   * @param value 对象
   * @return 字节数组
   */
  public static byte[] writeValueAsBytes(Object value) {
    byte[] bytes = null;
    try {
      bytes = mapper.writeValueAsBytes(value);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    return bytes;
  }

  /**
   * 格式化json字符串之后输出
   *
   * @param value 对象
   * @return json字符串
   */
  public static String writeValueWithPrettyPrinter(Object value) {
    String str = null;
    try {
      str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    return str;
  }

  /**
   * @param src   源
   * @param clazz class类型
   * @param <T>   泛型
   * @return 对象
   */
  public static <T> T fromJson(String src, Class<T> clazz) {
    T obj = null;
    try {
      obj = mapper.readValue(src, clazz);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    return obj;
  }

  /**
   * @param src          源
   * @param valueTypeRef 反射类型
   * @param <T>          泛型
   * @return 对象
   */
  public static <T> T fromJson(String src, TypeReference<T> valueTypeRef) {
    T obj = null;
    try {
      obj = mapper.readValue(src, valueTypeRef);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return obj;
  }

  /**
   * @param src   源
   * @param clazz 反射类型
   * @param <T>   泛型
   * @return 对象
   */
  public static <T> T fromJson(File src, Class<T> clazz) {
    T obj = null;
    try {
      obj = mapper.readValue(src, clazz);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return obj;
  }

  /**
   * @param src          源
   * @param valueTypeRef 反射类型
   * @param <T>          泛型
   * @return 对象
   */
  public static <T> T fromJson(File src, TypeReference<T> valueTypeRef) {
    T obj = null;
    try {
      obj = mapper.readValue(src, valueTypeRef);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return obj;
  }

  /**
   * @param src   源
   * @param clazz 反射类型
   * @param <T>   泛型
   * @return 对象
   */
  public static <T> T fromJson(byte[] src, Class<T> clazz) {
    T obj = null;
    try {
      obj = mapper.readValue(src, clazz);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return obj;
  }

  /**
   * @param src          源
   * @param valueTypeRef 反射类型
   * @param <T>          泛型
   * @return 对象
   */
  public static <T> T fromJson(byte[] src, TypeReference<T> valueTypeRef) {
    T obj = null;
    try {
      obj = mapper.readValue(src, valueTypeRef);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return obj;
  }

  /**
   * @param src   源
   * @param clazz 反射类型
   * @param <T>   泛型
   * @return 对象
   */
  public static <T> T fromJson(InputStream src, Class<T> clazz) {
    T obj = null;
    try {
      obj = mapper.readValue(src, clazz);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return obj;
  }

  /**
   * @param src          源
   * @param valueTypeRef 反射类型
   * @param <T>          泛型
   * @return 对象
   */
  public static <T> T fromJson(InputStream src, TypeReference<T> valueTypeRef) {
    T obj = null;
    try {
      obj = mapper.readValue(src, valueTypeRef);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return obj;
  }

  /**
   * @param src   源
   * @param clazz 反射类型
   * @param <T>   泛型
   * @return 对象
   */
  public static <T> T fromJson(Reader src, Class<T> clazz) {
    T obj = null;
    try {
      obj = mapper.readValue(src, clazz);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return obj;
  }

  /**
   * @param src          源
   * @param valueTypeRef 反射类型
   * @param <T>          泛型
   * @return 对象
   */
  public static <T> T fromJson(Reader src, TypeReference<T> valueTypeRef) {
    T obj = null;
    try {
      obj = mapper.readValue(src, valueTypeRef);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return obj;
  }
}

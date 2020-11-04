package org.fan.local.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.support.ConnectionPoolSupport;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.fan.local.utils.component.CostumerRedisCodec;

/**
 * 使用lettuce实现的redis客户端，用于 string-> object映射
 *
 * @version 1.0
 * @author: Fan
 * @date 2020.9.25 16:21
 */
public class LettuceRedisUtil {

  private static final RedisCodec<String, Object> redisCodec = new CostumerRedisCodec();
  private static final GenericObjectPool<StatefulRedisConnection<String, Object>> pool;
  private static final RedisClient client;
  private static final ObjectMapper mapper = new ObjectMapper();

  static {
    RedisURI redisURI = RedisURI.builder().withHost("127.0.0.1").withPort(6379).withPassword("123456")
        .withTimeout(Duration.ofSeconds(5)).build();
    client = RedisClient.create(redisURI);
    GenericObjectPoolConfig<StatefulRedisConnection<String, Object>> poolConfig = new GenericObjectPoolConfig<>();
    poolConfig.setMaxTotal(200);
    poolConfig.setMaxIdle(100);
    poolConfig.setMinIdle(20);
    pool = ConnectionPoolSupport.createGenericObjectPool(() -> client.connect(redisCodec), poolConfig);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  // ============================普通key操作=============================

  /**
   * 根据redis key获取值
   *
   * @param key key
   * @return value
   */
  public static Object get(String key) {
    RedisCommands<String, Object> commands = getRedisCommands();
    Object o;
    try {
      o = commands.get(key);
    } finally {
      commands.getStatefulConnection().close();
    }
    return o;
  }

  /**
   * 根据redis key获取值，返回的类型由入参指定
   *
   * @param key  key
   * @param type 类型
   * @param <T>  返回类型
   * @return value
   */
  public static <T> T get(String key, Class<T> type) {
    RedisCommands<String, Object> commands = getRedisCommands();
    Object o;
    try {
      o = commands.get(key);
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      commands.getStatefulConnection().close();
    }
    if (o == null) {
      return null;
    }
    if (o.getClass() == type) {
      return type.cast(o);
    } else if (o instanceof String) {
      try {
        return mapper.readValue(o.toString(), type);
      } catch (JsonProcessingException e) {
        throw new ClassCastException("an except occur when json string Convert to " + type.getName());
      }
    } else {
      throw new ClassCastException("type not match!");
    }
  }

  /**
   * 设置key的值
   *
   * @param key   key
   * @param value value
   */
  public static void set(String key, Object value) {
    RedisCommands<String, Object> commands = getRedisCommands();
    try {
      commands.set(key, value);
    } finally {
      commands.getStatefulConnection().close();
    }
  }

  /**
   * 设置key的值并且设置失效时间
   *
   * @param key     key
   * @param value   value
   * @param seconds 失效时间。单位：秒
   */
  public static void set(String key, Object value, long seconds) {
    RedisCommands<String, Object> commands = getRedisCommands();
    try {
      commands.setex(key, seconds, value);
    } finally {
      commands.getStatefulConnection().close();
    }
  }

  /**
   * 设置key的失效时间
   *
   * @param key     key
   * @param seconds 失效时间。单位：秒
   * @return 设置成功返回true。失败或者不能设置返回false
   */
  public static boolean expire(String key, long seconds) {
    RedisCommands<String, Object> commands = getRedisCommands();
    Boolean expire;
    try {
      expire = commands.expire(key, seconds);
    } finally {
      commands.getStatefulConnection().close();
    }
    return Optional.ofNullable(expire).orElse(false);
  }

  /**
   * 设置key的失效时间
   *
   * @param key          key
   * @param milliseconds 失效时间。单位：毫秒
   * @return 设置成功返回true。失败或者不能设置返回false
   */
  public static boolean pexpire(String key, long milliseconds) {
    RedisCommands<String, Object> commands = getRedisCommands();
    Boolean expire;
    try {
      expire = commands.pexpire(key, milliseconds);
    } finally {
      commands.getStatefulConnection().close();
    }
    return Optional.ofNullable(expire).orElse(false);
  }

  /**
   * 设置key的值并且设置失效时间
   *
   * @param key          key
   * @param milliseconds 失效时间。单位：毫秒
   * @param value        value
   */
  public static void psetex(String key, long milliseconds, Object value) {
    RedisCommands<String, Object> commands = getRedisCommands();
    try {
      commands.psetex(key, milliseconds, value);
    } finally {
      commands.getStatefulConnection().close();
    }
  }

  /**
   * 只有当key不存在的时候value才能被设置
   *
   * @param key   key
   * @param value value
   * @return 设置成功返回true，其他返回false。
   */
  public static boolean setnx(String key, Object value) {
    RedisCommands<String, Object> commands = getRedisCommands();
    Boolean result;
    try {
      result = commands.setnx(key, value);
    } finally {
      commands.getStatefulConnection().close();
    }
    return Optional.ofNullable(result).orElse(false);
  }

  /**
   * key自增1
   *
   * @param key key
   * @return 返回增长后的值
   */
  public static long incr(String key) {
    RedisCommands<String, Object> commands = getRedisCommands();
    Long incr;
    try {
      incr = commands.incr(key);
    } finally {
      commands.getStatefulConnection().close();
    }
    return Optional.ofNullable(incr).orElse(0L);
  }

  private static RedisCommands<String, Object> getRedisCommands() {
    StatefulRedisConnection<String, Object> connection;
    try {
      connection = pool.borrowObject();
    } catch (Exception e) {
      throw new RuntimeException("获取redis连接失败！" + e.getMessage());
    }
    return connection.sync();
  }

  protected static void close() {
    pool.close();
    CompletableFuture<Void> future = client.shutdownAsync();
    future.join();
    try {
      TimeUnit.SECONDS.sleep(10);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}

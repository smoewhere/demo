package org.fan.local.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.9.9 16:36
 */
public class RedisUtil {

  private static volatile RedisTemplate<String, String> REDIS_TEMPLATE;

  private static boolean inited = false;

  private RedisUtil() {
  }

  private static void init() {
    if (REDIS_TEMPLATE == null) {
      synchronized (RedisUtil.class) {
        if (REDIS_TEMPLATE == null) {
          initRedisTemplate();
          inited = true;
        }
      }
    }
  }

  private static void initRedisTemplate() {
    StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
    RedisConnectionFactory factory = createFactory();
    stringRedisTemplate.setConnectionFactory(factory);
    stringRedisTemplate.afterPropertiesSet();
    REDIS_TEMPLATE = stringRedisTemplate;
  }

  private static RedisConnectionFactory createFactory() {
    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
    config.setHostName("127.0.0.1");
    config.setPort(6379);
    config.setPassword(RedisPassword.of("lf0507"));
    config.setDatabase(1);
    GenericObjectPoolConfig<Object> poolConfig = new GenericObjectPoolConfig<>();
    poolConfig.setMaxIdle(100);
    poolConfig.setMinIdle(20);
    poolConfig.setMaxTotal(200);
    LettucePoolingClientConfiguration configuration = LettucePoolingClientConfiguration
        .builder().poolConfig(poolConfig).build();
    LettuceConnectionFactory factory = new LettuceConnectionFactory(config, configuration);
    factory.afterPropertiesSet();
    return factory;
  }

  /**
   * 指定缓存失效时间
   *
   * @param key  键
   * @param time 时间(秒)
   * @return 操作是否成功
   */
  public static boolean expire(String key, long time) {
    if (!inited) {
      init();
    }
    try {
      if (time > 0) {
        REDIS_TEMPLATE.expire(key, time, TimeUnit.SECONDS);
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 根据key获取过期时间
   *
   * @param key 键 不能为null
   * @return 时间(秒) 返回0代表为永久有效
   */
  public static Long getExpire(String key) {
    if (!inited) {
      init();
    }
    return REDIS_TEMPLATE.getExpire(Optional.of(key).get(), TimeUnit.SECONDS);
  }

  /**
   * 判断key是否存在
   *
   * @param key 键
   * @return true 存在 false不存在
   */
  public static Boolean hasKey(String key) {
    if (!inited) {
      init();
    }
    try {
      return REDIS_TEMPLATE.hasKey(key);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 删除缓存
   *
   * @param key 可以传一个值 或多个
   */
  @SuppressWarnings("unchecked")
  public static void del(String... key) {
    if (!inited) {
      init();
    }
    if (key != null && key.length > 0) {
      if (key.length == 1) {
        REDIS_TEMPLATE.delete(key[0]);
      } else {
        REDIS_TEMPLATE.delete(CollectionUtils.arrayToList(key));
      }
    }
  }
  // ============================String(字符串)=============================

  /**
   * 缓存获取，返回值为通用
   *
   * @param key 键
   * @return 值
   */
  public static String get(String key) {
    if (!inited) {
      init();
    }
    return key == null ? null : REDIS_TEMPLATE.opsForValue().get(key);
  }

  /**
   * 普通缓存放入
   *
   * @param key   键
   * @param value 值
   * @return true成功 false失败
   */
  public static boolean set(String key, String value) {
    if (!inited) {
      init();
    }
    try {
      REDIS_TEMPLATE.opsForValue().set(key, value);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public static boolean setNx(String key, String value) {
    if (!inited) {
      init();
    }
    try {
      return Optional.ofNullable(REDIS_TEMPLATE.opsForValue().setIfAbsent(key, value)).orElse(false);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 普通缓存放入并设置时间
   *
   * @param key   键
   * @param value 值
   * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
   * @return true成功 false 失败
   */
  public static boolean set(String key, String value, long time) {
    if (!inited) {
      init();
    }
    try {
      if (time > 0) {
        REDIS_TEMPLATE.opsForValue().set(key, value, time, TimeUnit.SECONDS);
      } else {
        set(key, value);
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 递增
   *
   * @param key   键
   * @param delta 要增加几(大于0)
   * @return 增加之后的值
   */
  public static Long incr(String key, long delta) {
    if (!inited) {
      init();
    }
    if (delta < 0) {
      throw new RuntimeException("递增因子必须大于0");
    }
    return REDIS_TEMPLATE.opsForValue().increment(key, delta);
  }

  /**
   * 递减
   *
   * @param key   键
   * @param delta 要减少几(小于0)
   * @return 减少之后的值
   */
  public static Long decr(String key, long delta) {
    if (!inited) {
      init();
    }
    if (delta < 0) {
      throw new RuntimeException("递减因子必须大于0");
    }
    return REDIS_TEMPLATE.opsForValue().increment(key, -delta);
  }
  // ================================Hash(哈希)=================================

  /**
   * HashGet
   *
   * @param key   键 不能为null
   * @param field 项 不能为null
   * @return 值
   */
  public static String hGet(String key, String field) {
    if (!inited) {
      init();
    }
    return (String) REDIS_TEMPLATE.opsForHash().get(key, field);
  }

  /**
   * 获取hashKey对应的所有键值
   *
   * @param key 键
   * @return 对应的多个键值
   */
  public static Map<String, String> hGetAll(String key) {
    if (!inited) {
      init();
    }
    Map<Object, Object> entries = REDIS_TEMPLATE.opsForHash().entries(key);
    HashMap<String, String> result = new HashMap<>();
    if (entries.size() > 0) {
      for (Entry<Object, Object> entry : entries.entrySet()) {
        result.put((String) entry.getKey(), (String) entry.getValue());
      }
    }
    return result;
  }

  /**
   * HashSet
   *
   * @param key 键
   * @param map 对应多个键值
   * @return true 成功 false 失败
   */
  public static boolean hMSet(String key, Map<String, String> map) {
    if (!inited) {
      init();
    }
    try {
      REDIS_TEMPLATE.opsForHash().putAll(key, map);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * HashSet 并设置时间
   *
   * @param key  键
   * @param map  对应多个键值
   * @param time 时间(秒)
   * @return true成功 false失败
   */
  public static boolean hMSet(String key, Map<String, String> map, long time) {
    if (!inited) {
      init();
    }
    try {
      REDIS_TEMPLATE.opsForHash().putAll(key, map);
      if (time > 0) {
        expire(key, time);
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 向一张hash表中放入数据,如果不存在将创建
   *
   * @param key   键
   * @param field 项
   * @param value 值
   * @return true 成功 false失败
   */
  public static boolean hset(String key, String field, String value) {
    if (!inited) {
      init();
    }
    try {
      REDIS_TEMPLATE.opsForHash().put(key, field, value);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 向一张hash表中放入数据,如果不存在将创建
   *
   * @param key   键
   * @param field 项
   * @param value 值
   * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
   * @return true 成功 false失败
   */
  public static boolean hset(String key, String field, String value, long time) {
    if (!inited) {
      init();
    }
    try {
      REDIS_TEMPLATE.opsForHash().put(key, field, value);
      if (time > 0) {
        expire(key, time);
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 删除hash表中的值
   *
   * @param key   键 不能为null
   * @param field 项 可以使多个 不能为null
   */
  public static long hdel(String key, String... field) {
    if (!inited) {
      init();
    }
    return REDIS_TEMPLATE.opsForHash().delete(key, (Object) field);
  }

  /**
   * 判断hash表中是否有该项的值
   *
   * @param key   键 不能为null
   * @param field 项 不能为null
   * @return true 存在 false不存在
   */
  public static boolean hHasKey(String key, String field) {
    if (!inited) {
      init();
    }
    return REDIS_TEMPLATE.opsForHash().hasKey(key, field);
  }

  /**
   * hash递增 如果不存在,就会创建一个 并把新增后的值返回，用于浮点型
   *
   * @param key   键
   * @param field 项
   * @param by    要增加几(可以为负数，减少)
   * @return 增加之后的值
   */
  public static Double hincr(String key, String field, double by) {
    if (!inited) {
      init();
    }
    return REDIS_TEMPLATE.opsForHash().increment(key, field, by);
  }

  /**
   * hash递增 如果不存在,就会创建一个 并把新增后的值返回，用于整形
   *
   * @param key   键
   * @param field 项
   * @param by    要增加几(可以为负数，减少)
   * @return 增加之后的值
   */
  public static Long hincr(String key, String field, long by) {
    if (!inited) {
      init();
    }
    return REDIS_TEMPLATE.opsForHash().increment(key, field, by);
  }

  // ============================Set(集合)=============================

  /**
   * 根据key获取Set中的所有值
   *
   * @param key 键
   * @return set集合
   */
  public static Set<String> sGet(String key) {
    if (!inited) {
      init();
    }
    try {
      return REDIS_TEMPLATE.opsForSet().members(key);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 根据value从一个set中查询,是否存在
   *
   * @param key   键
   * @param value 值
   * @return true 存在 false不存在
   */
  public static boolean sHasKey(String key, Object value) {
    if (!inited) {
      init();
    }
    if (key == null) {
      return false;
    }
    try {
      Boolean result = REDIS_TEMPLATE.opsForSet().isMember(key, value);
      return result != null ? result : false;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 将数据放入set缓存
   *
   * @param key    键
   * @param values 值 可以是多个
   * @return 成功个数
   */
  public static long sSet(String key, String... values) {
    if (!inited) {
      init();
    }
    try {
      return Optional.ofNullable(REDIS_TEMPLATE.opsForSet().add(key, values)).orElse(0L);
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  /**
   * 将set数据放入缓存
   *
   * @param key    键
   * @param time   时间(秒)
   * @param values 值 可以是多个
   * @return 成功个数
   */
  public static long sSetAndTime(String key, long time, String... values) {
    if (!inited) {
      init();
    }
    try {
      Long count = Optional.ofNullable(REDIS_TEMPLATE.opsForSet().add(key, values)).orElse(0L);
      if (time > 0) {
        expire(key, time);
      }
      return count;
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  /**
   * 获取set缓存的长度
   *
   * @param key 键
   * @return 长度
   */
  public static long sGetSetSize(String key) {
    if (!inited) {
      init();
    }
    try {
      return Optional.ofNullable(REDIS_TEMPLATE.opsForSet().size(key)).orElse(0L);
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  /**
   * 移除值为value的
   *
   * @param key    键
   * @param values 值 可以是多个
   * @return 移除的个数
   */
  public static long setRemove(String key, Object... values) {
    if (!inited) {
      init();
    }
    try {
      return Optional.ofNullable(REDIS_TEMPLATE.opsForSet().remove(key, values)).orElse(0L);
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }
  // ===============================List(列表)=================================

  /**
   * 获取list缓存的内容
   *
   * @param key   键
   * @param start 开始
   * @param end   结束 0 到 -1代表所有值
   * @return
   */
  public static List<String> lRange(String key, long start, long end) {
    if (!inited) {
      init();
    }
    try {
      return REDIS_TEMPLATE.opsForList().range(key, start, end);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 获取list缓存的长度
   *
   * @param key 键
   * @return 数量
   */
  public static Long lLen(String key) {
    if (!inited) {
      init();
    }
    try {
      return REDIS_TEMPLATE.opsForList().size(key);
    } catch (Exception e) {
      e.printStackTrace();
      return 0L;
    }
  }

  /**
   * 通过索引 获取list中的值
   *
   * @param key   键
   * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
   * @return
   */
  public static String lIndex(String key, long index) {
    if (!inited) {
      init();
    }
    try {
      return REDIS_TEMPLATE.opsForList().index(key, index);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 将list放入缓存
   *
   * @param key   键
   * @param value 值
   * @return
   */
  public static boolean lPush(String key, String value) {
    if (!inited) {
      init();
    }
    try {
      REDIS_TEMPLATE.opsForList().leftPush(key, value);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 将值放入一个已经存在的list头部
   *
   * @param key   键
   * @param value 值
   * @return 返回是否成功
   */
  public static boolean lPushX(String key, String value) {
    if (!inited) {
      init();
    }
    try {
      REDIS_TEMPLATE.opsForList().leftPushIfPresent(key, value);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 将list放入缓存
   *
   * @param key   键
   * @param value 值
   * @return 结果数量
   */
  public static Long lPushAll(String key, Collection<String> value) {
    if (!inited) {
      init();
    }
    try {
      return REDIS_TEMPLATE.opsForList().leftPushAll(key, value);
    } catch (Exception e) {
      e.printStackTrace();
      return 0L;
    }
  }

  /**
   * 通过索引设置列表元素的值
   *
   * @param key   键
   * @param value 值
   * @param index 索引
   */
  public static void lSet(String key, int index, String value) {
    if (!inited) {
      init();
    }
    try {
      REDIS_TEMPLATE.opsForList().set(key, index, value);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 移除N个值为value
   *
   * @param key   键
   * @param count count > 0 : 从表头开始向表尾搜索，移除与 VALUE 相等的元素，数量为 COUNT 。 count < 0 : 从表尾开始向表头搜索，移除与 VALUE 相等的元素，数量为 COUNT
   *              的绝对值。 count = 0 : 移除表中所有与 VALUE 相等的值。
   * @param value 值
   * @return 移除的个数
   */
  public static Long lRem(String key, long count, Object value) {
    if (!inited) {
      init();
    }
    try {
      return REDIS_TEMPLATE.opsForList().remove(key, count, value);
    } catch (Exception e) {
      e.printStackTrace();
      return 0L;
    }
  }

  /**
   * 删除并返回存储在key处的列表的第一个元素。
   *
   * @param key key
   * @return 第一个元素
   */
  public static String lPop(String key) {
    if (!inited) {
      init();
    }
    try {
      return REDIS_TEMPLATE.opsForList().leftPop(key);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 将list放入缓存
   *
   * @param key   键
   * @param value 值
   * @return 放入的长度
   */
  public static Long rPush(String key, String value) {
    if (!inited) {
      init();
    }
    try {
      return REDIS_TEMPLATE.opsForList().rightPush(key, value);
    } catch (Exception e) {
      e.printStackTrace();
      return 0L;
    }
  }

  /**
   * 删除并返回存储在key处的列表的第一个元素。
   *
   * @param key key
   * @return 返回值，如果列表已经为空则返回null
   */
  public static String rPop(String key) {
    if (!inited) {
      init();
    }
    try {
      return REDIS_TEMPLATE.opsForList().rightPop(key);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 将值天添加到一个已经存在的list尾部
   *
   * @param key   键
   * @param value 值
   * @return 是否成功
   */
  public static boolean rPushX(String key, String value) {
    try {
      REDIS_TEMPLATE.opsForList().rightPushIfPresent(key, value);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 将list放入缓存
   *
   * @param key   键
   * @param value 值
   * @return 结果数量
   */
  public static Long rPushAll(String key, Collection<String> value) {
    if (!inited) {
      init();
    }
    try {
      return REDIS_TEMPLATE.opsForList().rightPushAll(key, value);
    } catch (Exception e) {
      e.printStackTrace();
      return 0L;
    }
  }

  public static Boolean isExit(String key) {
    if (!inited) {
      init();
    }
    return REDIS_TEMPLATE.hasKey(key);
  }


}

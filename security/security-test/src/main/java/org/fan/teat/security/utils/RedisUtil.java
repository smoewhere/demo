package org.fan.teat.security.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.yaml.snakeyaml.Yaml;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.9.28 15:04
 */
public class RedisUtil {

    private static final Logger log = LoggerFactory.getLogger(RedisUtil.class);

    private static final ConcurrentHashMap<Integer, RedisUtil> redisClientWithDatabase =
            new ConcurrentHashMap<>(4);

    private StringRedisTemplate REDIS_TEMPLATE;

    private static final Properties prop = new Properties();

    private static boolean inited = false;

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final int DEFAULT_MAX_IDLE = 100;
    private static final int DEFAULT_MIN_IDLE = 20;
    private static final int DEFAULT_MAX_ACTIVE = 200;
    private static final long DEFAULT_READ_TIMEOUT_MS = 10000;
    public static int DEFAULT_DATABASE = 0;

    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        initProp();
    }

    private static void initProp() {
        Yaml yaml = new Yaml();
        URL resource = RedisUtil.class.getResource("/conf/redisConfig.yaml");
        URI uri;
        try {
            uri = resource.toURI();
        } catch (URISyntaxException e) {
            log.error("init failed", e);
            return;
        }
        Properties properties = new Properties();
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(uri)))) {
            Map<String, Map<String, Object>> obj = yaml.load(reader);
            Map<String, Object> redisUtil = obj.get("redisUtil");
            Integer database = (Integer) redisUtil.get("database");
            if (database != null) {
                DEFAULT_DATABASE = database;
            }
            properties.putAll(redisUtil);
        } catch (IOException e) {
            log.error("init failed", e);
            return;
        }
        prop.putAll(properties);
        inited = true;
    }

    private RedisUtil() {
    }

    public static RedisUtil getInstance() {
        return getInstance(DEFAULT_DATABASE);
    }

    public static RedisUtil getInstance(int dataBase) {
        if (dataBase < 0) {
            throw new IllegalArgumentException("dataBase can't be negative！");
        }
        if (!redisClientWithDatabase.containsKey(dataBase)) {
            synchronized (redisClientWithDatabase) {
                if (!redisClientWithDatabase.containsKey(dataBase)) {
                    init(dataBase);
                }
            }
        }
        return redisClientWithDatabase.get(dataBase);
    }

    private static void init(int dataBase) {
        if (!inited) {
            throw new UnsupportedOperationException("prop is not init！");
        }
        RedisUtil redisUtil = new RedisUtil();
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        RedisConnectionFactory factory = createFactory(dataBase);
        stringRedisTemplate.setConnectionFactory(factory);
        stringRedisTemplate.afterPropertiesSet();
        redisUtil.REDIS_TEMPLATE = stringRedisTemplate;
        redisClientWithDatabase.putIfAbsent(dataBase, redisUtil);
    }

    private static RedisConnectionFactory createFactory(int dataBase) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        if (!prop.containsKey("host")) {
            throw new NullPointerException("redis host can not be null!");
        }
        config.setHostName(prop.getProperty("host"));
        if (!prop.containsKey("port")) {
            throw new NullPointerException("redis port can not be null!");
        }
        config.setPort((Integer) prop.get("port"));
        if (prop.containsKey("password")) {
            config.setPassword(RedisPassword.of(prop.getProperty("password")));
        }
        config.setDatabase(dataBase);
        GenericObjectPoolConfig<Object> poolConfig = new GenericObjectPoolConfig<>();
        if (prop.containsKey("max-idle")) {
            poolConfig.setMaxIdle((Integer) prop.get("max-idle"));
        } else {
            poolConfig.setMaxIdle(DEFAULT_MAX_IDLE);
        }
        if (prop.containsKey("min-idle")) {
            poolConfig.setMinIdle((Integer) prop.get("min-idle"));
        } else {
            poolConfig.setMinIdle(DEFAULT_MIN_IDLE);
        }
        if (prop.containsKey("max-active")) {
            poolConfig.setMaxTotal((Integer) prop.get("max-active"));
        } else {
            poolConfig.setMaxTotal(DEFAULT_MAX_ACTIVE);
        }
        JedisClientConfiguration.JedisClientConfigurationBuilder builder = JedisClientConfiguration.builder();
        if (prop.containsKey("timeout")) {
            builder.readTimeout(Duration.ofMillis(Long.parseLong(String.valueOf(prop.get("timeout")))));
        } else {
            builder.readTimeout(Duration.ofMillis(DEFAULT_READ_TIMEOUT_MS));
        }
        JedisClientConfiguration clientConfiguration = builder.usePooling()
                .poolConfig(poolConfig).build();
        JedisConnectionFactory factory = new JedisConnectionFactory(config, clientConfiguration);
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
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                REDIS_TEMPLATE.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("redisUtil error", e);
            return false;
        }
    }

    /**
     * 根据key获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return Optional.ofNullable(REDIS_TEMPLATE.getExpire(Optional.of(key).get(), TimeUnit.SECONDS))
                .orElse(0L);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return Optional.ofNullable(REDIS_TEMPLATE.hasKey(key)).orElse(false);
        } catch (Exception e) {
            log.error("redisUtil error", e);
            return false;
        }
    }

    public Set<String> keys(String pattern) {
        return Optional.ofNullable(REDIS_TEMPLATE.keys(pattern)).orElse(new HashSet<>());
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                REDIS_TEMPLATE.delete(key[0]);
            } else {
                REDIS_TEMPLATE.delete(Arrays.asList(key));
            }
        }
    }
    // ============================String(字符串)=============================

    /**
     * 缓存获取，返回值为通用object
     *
     * @param key 键
     * @return 值
     */
    public String get(String key) {
        return key == null ? null : REDIS_TEMPLATE.opsForValue().get(key);
    }

    /**
     * 缓存获取,返回值为指定的对象
     *
     * @param key 键
     * @return 值
     */
    public <T> T get(String key, Class<T> type) {
        String o = get(key);
        return transformObj(type, o);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        Objects.requireNonNull(value);
        try {
            String v = transform(value);
            REDIS_TEMPLATE.opsForValue().set(key, v);
            return true;
        } catch (Exception e) {
            log.error("set error", e);
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key     键
     * @param value   值
     * @param seconds 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long seconds) {
        try {
            String v = transform(value);
            if (seconds > 0) {
                REDIS_TEMPLATE.opsForValue().set(key, v, seconds, TimeUnit.SECONDS);
            } else {
                set(key, v);
            }
            return true;
        } catch (Exception e) {
            log.error("set error", e);
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return 增加之后的value
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new IllegalArgumentException("递增因子必须大于0");
        }
        return Optional.ofNullable(REDIS_TEMPLATE.opsForValue().increment(key, delta)).orElse(0L);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return 减少之后的值
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return Optional.ofNullable(REDIS_TEMPLATE.opsForValue().decrement(key, delta)).orElse(0L);
    }
    // ================================Hash(哈希)=================================

    /**
     * HashGet
     *
     * @param key   键 不能为null
     * @param field 项 不能为null
     * @return 值
     */
    public String hGet(String key, String field) {
        return (String) REDIS_TEMPLATE.opsForHash().get(key, field);
    }

    /**
     * HashGet
     *
     * @param key   键 不能为null
     * @param field 项 不能为null
     * @return 值
     */
    public <T> T hGet(String key, String field, Class<T> type) {
        String o = hGet(key, field);
        return transformObj(type, o);
    }


    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<String, String> hGetAll(String key) {
        Map<Object, Object> entries = REDIS_TEMPLATE.opsForHash().entries(key);
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            result.put((String) entry.getKey(), (String) entry.getValue());
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
    public boolean hMSet(String key, Map<String, String> map) {
        try {
            REDIS_TEMPLATE.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("redisUtil error", e);
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
    public boolean hMSet(String key, Map<String, String> map, long time) {
        try {
            REDIS_TEMPLATE.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("redisUtil error", e);
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
    public boolean hset(String key, String field, Object value) {
        try {
            String v = transform(value);
            REDIS_TEMPLATE.opsForHash().put(key, field, v);
            return true;
        } catch (Exception e) {
            log.error("redisUtil error", e);
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
    public boolean hset(String key, String field, Object value, long time) {
        try {
            String v = transform(value);
            REDIS_TEMPLATE.opsForHash().put(key, field, v);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("redisUtil error", e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key   键 不能为null
     * @param field 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... field) {
        REDIS_TEMPLATE.opsForHash().delete(key, field);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key   键 不能为null
     * @param field 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String field) {
        return REDIS_TEMPLATE.opsForHash().hasKey(key, field);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回，用于浮点型
     *
     * @param key   键
     * @param field 项
     * @param by    要增加几(可以为负数，减少)
     * @return 增长之后的数
     */
    public double hincr(String key, String field, double by) {
        return REDIS_TEMPLATE.opsForHash().increment(key, field, by);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回，用于整形
     *
     * @param key   键
     * @param field 项
     * @param by    要增加几(可以为负数，减少)
     * @return 增长之后的数
     */
    public long hincr(String key, String field, long by) {
        return REDIS_TEMPLATE.opsForHash().increment(key, field, by);
    }

    // ============================Set(集合)=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<String> sGet(String key) {
        try {
            return REDIS_TEMPLATE.opsForSet().members(key);
        } catch (Exception e) {
            log.error("redisUtil error", e);
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
    public boolean sHasKey(String key, Object value) {
        try {
            String v = transform(value);
            Boolean result = REDIS_TEMPLATE.opsForSet().isMember(key, v);
            return Optional.ofNullable(result).orElse(false);
        } catch (Exception e) {
            log.error("redisUtil error", e);
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
    public long sSet(String key, String... values) {
        try {
            Long result = REDIS_TEMPLATE.opsForSet().add(key, values);
            return Optional.ofNullable(result).orElse(0L);
        } catch (Exception e) {
            log.error("redisUtil error", e);
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
    public long sSetAndTime(String key, long time, String... values) {
        try {
            Long count = REDIS_TEMPLATE.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return Optional.ofNullable(count).orElse(0L);
        } catch (Exception e) {
            log.error("redisUtil error", e);
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return size
     */
    public long sGetSetSize(String key) {
        try {
            return Optional.ofNullable(REDIS_TEMPLATE.opsForSet().size(key)).orElse(0L);
        } catch (Exception e) {
            log.error("redisUtil error", e);
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
    public long setRemove(String key, Object... values) {
        try {
            return Optional.ofNullable(REDIS_TEMPLATE.opsForSet().remove(key, values))
                    .orElse(0L);
        } catch (Exception e) {
            log.error("redisUtil error", e);
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
     * @return list
     */
    public List<String> lRange(String key, long start, long end) {
        try {
            return Optional.ofNullable(REDIS_TEMPLATE.opsForList().range(key, start, end))
                    .orElse(new ArrayList<>());
        } catch (Exception e) {
            log.error("redisUtil error", e);
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return 数量
     */
    public long lLen(String key) {
        try {
            return Optional.ofNullable(REDIS_TEMPLATE.opsForList().size(key))
                    .orElse(0L);
        } catch (Exception e) {
            log.error("redisUtil error", e);
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return value
     */
    public String lIndex(String key, long index) {
        try {
            return REDIS_TEMPLATE.opsForList().index(key, index);
        } catch (Exception e) {
            log.error("redisUtil error", e);
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return 是否成功
     */
    public boolean lPush(String key, String value) {
        try {
            REDIS_TEMPLATE.opsForList().leftPush(key, value);
            return true;
        } catch (Exception e) {
            log.error("redisUtil error", e);
            return false;
        }
    }

    /**
     * 将值放入一个已经存在的list头部
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lPushX(String key, String value) {
        try {
            REDIS_TEMPLATE.opsForList().leftPushIfPresent(key, value);
            return true;
        } catch (Exception e) {
            log.error("redisUtil error", e);
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
    public long lPushAll(String key, Collection<String> value) {
        try {
            return Optional.ofNullable(REDIS_TEMPLATE.opsForList().leftPushAll(key, value))
                    .orElse(0L);
        } catch (Exception e) {
            log.error("redisUtil error", e);
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
    public void lSet(String key, int index, String value) {
        try {
            REDIS_TEMPLATE.opsForList().set(key, index, value);
        } catch (Exception e) {
            log.error("redisUtil error", e);
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count count > 0 : 从表头开始向表尾搜索，移除与 VALUE 相等的元素，数量为 COUNT 。
     *              count < 0 : 从表尾开始向表头搜索，移除与 VALUE 相等的元素，数量为 COUNT 的绝对值。
     *              count = 0 : 移除表中所有与 VALUE 相等的值。
     * @param value 值
     * @return 移除的个数
     */
    public long lRem(String key, long count, String value) {
        try {
            return Optional.ofNullable(REDIS_TEMPLATE.opsForList().remove(key, count, value)).orElse(0L);
        } catch (Exception e) {
            log.error("redisUtil error", e);
            return 0L;
        }
    }

    /**
     * 删除并返回存储在key处的列表的第一个元素。
     *
     * @param key key
     * @return value
     */
    public String lPop(String key) {
        try {
            return REDIS_TEMPLATE.opsForList().leftPop(key);
        } catch (Exception e) {
            log.error("redisUtil error", e);
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return 放入个数
     */
    public long rPush(String key, String value) {
        try {
            return Optional.ofNullable(REDIS_TEMPLATE.opsForList().rightPush(key, value))
                    .orElse(0L);
        } catch (Exception e) {
            log.error("redisUtil error", e);
            return 0L;
        }
    }

    /**
     * 删除并返回存储在key处的列表的第一个元素。
     *
     * @param key key
     * @return 返回值，如果列表已经为空则返回null
     */
    public Object rPop(String key) {
        try {
            return REDIS_TEMPLATE.opsForList().rightPop(key);
        } catch (Exception e) {
            log.error("redisUtil error", e);
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
    public boolean rPushX(String key, String value) {
        try {
            REDIS_TEMPLATE.opsForList().rightPushIfPresent(key, value);
            return true;
        } catch (Exception e) {
            log.error("redisUtil error", e);
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
    public long rPushAll(String key, Collection<String> value) {
        try {
            return Optional.ofNullable(REDIS_TEMPLATE.opsForList().rightPushAll(key, value)).orElse(0L);
        } catch (Exception e) {
            log.error("redisUtil error", e);
            return 0L;
        }
    }

    public boolean isExit(String key) {
        return Optional.ofNullable(REDIS_TEMPLATE.hasKey(key)).orElse(false);
    }

    private static String transform(Object value) throws JsonProcessingException {
        String v = "";
        if (value.getClass() == String.class) {
            v = (String) value;
        } else {
            v = mapper.writeValueAsString(value);
        }
        return v;
    }

    private <T> T transformObj(Class<T> type, String o) {
        if (o == null) {
            return null;
        }

        if (type == String.class) {
            return type.cast(o);
        } else {
            try {
                return mapper.readValue(o, type);
            } catch (JsonProcessingException e) {
                throw new ClassCastException("an except occur when json string Convert to " + type.getName());
            }
        }
    }

}

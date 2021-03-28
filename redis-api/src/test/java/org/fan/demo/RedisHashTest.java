package org.fan.demo;

import io.lettuce.core.KeyValue;
import io.lettuce.core.MapScanCursor;
import io.lettuce.core.RedisClient;
import io.lettuce.core.ScanCursor;
import io.lettuce.core.api.sync.RedisCommands;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * redis的hash api操作demo
 *
 * @author Fan
 * @version 1.0
 * @date 2021.3.4 22:01
 */
public class RedisHashTest {

  private static final Logger log = LoggerFactory.getLogger(RedisHashTest.class);

  private static final RedisClient REDIS_CLIENT = RedisClientFactory.getRedisClient();

  private static final RedisCommands<String, String> COMMANDS = REDIS_CLIENT.connect().sync();

  private static final String key = "test:hash:testHash";

  //@AfterAll
  static void afterAll() {
    COMMANDS.del(key);
    REDIS_CLIENT.shutdown();
  }

  //@BeforeAll
  static void testAdd(){
    for (int i = 0; i < 200; i++) {
      COMMANDS.hset(key, "field" + i, "value" + i);
    }
  }

  @Test
  public void testHash(){
    // 根据field获取key中的value
    String field0 = COMMANDS.hget(key, "field0");
    log.info("[RedisHashTest.testHash] hGet field0 is {}", field0);
    // 类似setnx，如果不存在则设置value，如果存在则不进行操作
    Boolean result = COMMANDS.hsetnx(key, "field0", "value");
    log.info("[RedisHashTest.testHash] setnx result is {}", result);
    // 获取hash的长度
    Long len = COMMANDS.hlen(key);
    log.info("[RedisHashTest.testHash] hash len is {}", len);
    // 获取hash中filed里存储的字符串的长度
    len = COMMANDS.hstrlen(key, "field0");
    log.info("[RedisHashTest.testHash] field:{} 对应的value长度为{}", "field0",len);

  }

  /**
   * hash类型的批量操作
   */
  @Test
  public void testHashM(){
    HashMap<String, String> hashMap = new HashMap<>();
    for (int i = 0; i < 200; i++) {
      hashMap.put("field:" + i, "value:" + i);
    }
    COMMANDS.hmset(key, hashMap);
    List<KeyValue<String, String>> valueList = COMMANDS.hmget(key, "field:1");
    valueList.forEach(value->{
      log.info("field is {}, value is {}", value.getKey(), value.getValue());
    });
  }

  /**
   * scan的hash版本
   */
  @Test
  public void testHashScan(){
    MapScanCursor<String, String> scanCursor = new MapScanCursor<>();
    scanCursor.setCursor("0");
    do {
      scanCursor = COMMANDS.hscan(key, scanCursor);
      Map<String, String> result = scanCursor.getMap();
      result.forEach((field, value)-> System.out.println(MessageFormat
          .format("{0}:{1}",field, value)));
    } while (!scanCursor.isFinished());
  }

}

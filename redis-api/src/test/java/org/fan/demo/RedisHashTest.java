package org.fan.demo;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.jupiter.api.AfterAll;
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

  private final String key = "test:hash:testHash";

  @AfterAll
  static void afterAll() {
    REDIS_CLIENT.shutdown();
  }

  @Test
  public void testAdd(){
    for (int i = 0; i < 200; i++) {
      COMMANDS.hset(key, "field" + i, "value" + i);
    }
  }

  @Test
  public void testHash(){
    String field0 = COMMANDS.hget(key, "field0");
    log.info("[RedisHashTest.testHash] hGet field0 is {}", field0);
    Boolean result = COMMANDS.hsetnx(key, "field0", "value");
    log.info("[RedisHashTest.testHash] setnx result is {}", result);

  }

}

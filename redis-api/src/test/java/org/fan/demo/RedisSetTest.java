package org.fan.demo;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version 1.0
 * @author: Fan
 * @date 2021/3/3 14:43
 */
public class RedisSetTest {

  private static final Logger log = LoggerFactory.getLogger(RedisSetTest.class);

  private static final RedisClient REDIS_CLIENT = RedisClientFactory.getRedisClient();

  private static final RedisCommands<String, String> COMMANDS = REDIS_CLIENT.connect().sync();

  @AfterAll
  static void afterAll(){
    REDIS_CLIENT.shutdown();
  }

  @Test
  public void testSet(){
    String key = "test:set:testSet";
    COMMANDS.sadd(key, "jack");
    COMMANDS.sadd(key, "tom");
    COMMANDS.sadd(key, "tom");
  }

}

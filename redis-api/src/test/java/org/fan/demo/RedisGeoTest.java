package org.fan.demo;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fan
 * @version 1.0
 * @date 2021/4/6 22:51
 */
public class RedisGeoTest {

  private static final Logger log = LoggerFactory.getLogger(RedisGeoTest.class);

  private static final RedisClient REDIS_CLIENT = RedisClientFactory.getRedisClient();

  private static final RedisCommands<String, String> COMMANDS = REDIS_CLIENT.connect().sync();

  @AfterAll
  static void afterAll(){
    REDIS_CLIENT.shutdown();
  }

  @Test
  public void testGeo(){
    Long geoadd = COMMANDS.geoadd("map", 13.361389, 38.115556, "Palermo");
    log.info("[RedisGeoTest.testGeo] add {}", geoadd);
  }

}

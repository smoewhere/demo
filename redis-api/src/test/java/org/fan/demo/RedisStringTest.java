package org.fan.demo;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.protocol.CommandArgs;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * redis普通key-value键值对的API使用demo,基于redis 5
 *
 * @version 1.0
 * @author: Fan
 * @date 2021/3/3 11:26
 */
public class RedisStringTest {

  private static final Logger log = LoggerFactory.getLogger(RedisStringTest.class);

  private static final RedisClient REDIS_CLIENT = RedisClientFactory.getRedisClient();

  @AfterAll
  static void afterAll(){
    REDIS_CLIENT.shutdown();
  }

  @Test
  public void testGetSet(){
    RedisCommands<String, String> commands = REDIS_CLIENT.connect().sync();
    String result = commands.set("test:key:testGetSet", "jack");
    log.info("[RedisStringTest.testGetSet] set key {}", result);
    result = commands.get("test:key:testGetSet");
    log.info("[RedisStringTest.testGetSet] get key {}", result);
  }

  @Test
  public void testGet(){
    String key = "test:key:testGet";
    String value = "fan";
    RedisCommands<String, String> commands = REDIS_CLIENT.connect().sync();
    commands.set(key, value);
    log.info("[RedisStringTest.testGet] set value {}", value);
    String result = commands.get(key);
    log.info("[RedisStringTest.testGet] the API get result is {}", result);
    Long bitResult = commands.getbit(key, 1L);
    log.info("[RedisStringTest.testGet] GETBIT key offset: {}", bitResult);
    // GETDEL是redis 6.2.0之后支持的
    /*result = commands.dispatch(OtherCommandType.GETDEL, new StatusOutput<>(StringCodec.UTF8),
        new CommandArgs<>(StringCodec.UTF8).addKey(key));
    log.info("[RedisStringTest.testGet] GETDEL key result: {}", result);
    result = commands.get(key);
    log.info("[RedisStringTest.testGet] after GETDEL, get {} result is {}", key, result);*/
    result = commands.getrange(key, 0, 1);
    log.info("[RedisStringTest.testGet] getrange result: {}", result);
    result = commands.getset(key, value + "new");
    String newValue = commands.get(key);
    log.info("[RedisStringTest.testGet] getset old value: {}; new value {}", result, newValue);
  }

}

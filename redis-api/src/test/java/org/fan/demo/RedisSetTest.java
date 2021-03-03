package org.fan.demo;

import io.lettuce.core.RedisClient;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.ScanCursor;
import io.lettuce.core.ScoredValueScanCursor;
import io.lettuce.core.ValueScanCursor;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.output.ValueStreamingChannel;
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

  private final String key = "test:set:testSet";

  @AfterAll
  static void afterAll() {
    REDIS_CLIENT.shutdown();
  }

  @Test
  public void testSAdd() {
    String key = "test:set:testSet";
    for (int i = 0; i < 1000; i++) {
      COMMANDS.sadd(key, "jac" + i);
    }
  }

  /**
   * 对redis中set的操作，set为无序不重复集合
   */
  @Test
  public void testSet() {

    // 添加元素
    COMMANDS.sadd(key, "jack");
    COMMANDS.sadd(key, "tom");
    COMMANDS.sadd(key, "tom");
    // 返回set中数据的个数
    log.info("[RedisSetTest.testSet] nums {}", COMMANDS.scard(key));
    // 随机从集合中删除一个或多个值 note：随机，多个（key后面添加count参数）
    String pop = COMMANDS.spop(key);
    log.info("[RedisSetTest.testSet] pop value {}", pop);
    // 返回所有的成员
    COMMANDS.smembers((value) -> log.info("[RedisSetTest.testSet] member is {}", value), key);
    COMMANDS.sadd(key, "jack");
    COMMANDS.sadd(key, "tom");
    COMMANDS.sadd(key, "tom");
    // 判断元素是否在集合内
    log.info("[RedisSetTest.testSet] jack is member {}, jack2 is member {}", COMMANDS.sismember(key, "jack"),
        COMMANDS.sismember(key, "jack2"));
    // 行为和spop类似，不过不会移除成员，当count没有指定，随机返回一个成员，当count指定的情况
    // 如果count为正数，返回不重复元素的数组，当count为负数，会返回可重复的值
    COMMANDS.srandmember((value -> log.info("[RedisSetTest.testSet] member is {}", value)), key, -5);
    // 移除指定成员
    COMMANDS.srem(key, "jack");
    log.info("[RedisSetTest.testSet] remove jack");
    COMMANDS.smembers((value) -> log.info("[RedisSetTest.testSet] member is {}", value), key);
    log.info("[RedisSetTest.testSet] zscan start");
  }

  /**
   * 基于scan的操作，每次迭代，防止一下子取出大量值导致阻塞
   */
  @Test
  public void testZScan(){
    ValueScanCursor<String> zscan = null;
    int size = 100;
    do {
      if (zscan == null) {
        zscan = COMMANDS
            .sscan(key, new ScanArgs().match("jac*").limit(size));
      } else {
        zscan = COMMANDS
            .sscan(key,zscan , new ScanArgs().match("jac*").limit(size));
      }
      zscan.getValues().forEach(System.out::println);
    } while (!zscan.isFinished());
  }

}

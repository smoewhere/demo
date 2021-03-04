package org.fan.demo;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import java.time.Duration;

/**
 *
 *
 * @version 1.0
 * @author: Fan
 * @date 2021/3/3 10:22
 */
public class RedisClientFactory {

  private static final RedisClient REDIS_CLIENT;

  static {
    RedisURI redisUri = RedisURI.builder().withHost("127.0.0.1").withPort(6379)
        .withPassword("fsj@123456".toCharArray())
        .withTimeout(Duration.ofSeconds(5)).build();
    REDIS_CLIENT = RedisClient.create(redisUri);
  }

  private RedisClientFactory(){
    throw new UnsupportedOperationException();
  }

  public static RedisClient getRedisClient(){
    return REDIS_CLIENT;
  }

}

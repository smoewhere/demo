package org.fan.teat.security.config.redis;

import java.time.Duration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.Cache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.cache.SpringCacheBasedUserCache;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.10.26 18:24
 */
@Configuration(proxyBeanMethods = false)
public class RedisConfig {

  /**
   * spring创建redis缓存的时候用到的redis配置，如果没有，则使用defaultCacheConfig.
   * <p>
   * 详细请看 @see {@link RedisCacheConfiguration}. 这里主要配置了：
   * <p>
   * 1. 配置了value的序列化方法，默认是用JdkSerializationRedisSerializer.
   * <p>
   * 2. 配置了缓存失效时间，30天.
   *
   * @return RedisCacheConfiguration
   */
  @Bean
  public RedisCacheConfiguration redisCacheConfiguration() {
    RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
    config = config.entryTtl(Duration.ofDays(30));
    config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
        new GenericJackson2JsonRedisSerializer()));
    return config;
  }

  @Bean
  public UserCache userCache(RedisCacheManager cacheManager) {
    Cache cache = cacheManager.getCache("user");
    if (cache == null) {
      throw new RuntimeException("cache is null");
    }
    return new SpringCacheBasedUserCache(cache);
  }

}

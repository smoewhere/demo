package org.fan.demo.cachedemo.config.cache;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;

/**
 * @author Fan
 * @version 1.0
 * @date 2021.1.2 21:40
 */
public class MyCacheManager extends RedissonSpringCacheManager {

  private long ttl;

  private long maxIdleTime = 0;

  private int maxSize = 1000;

  private TimeUnit unit;


  public MyCacheManager(RedissonClient redisson) {
    super(redisson);
  }

  public MyCacheManager(RedissonClient redisson,
      Map<String, ? extends CacheConfig> config) {
    super(redisson, config);
  }

  public MyCacheManager(RedissonClient redisson,
      Map<String, ? extends CacheConfig> config, Codec codec) {
    super(redisson, config, codec);
  }

  public MyCacheManager(RedissonClient redisson, String configLocation) {
    super(redisson, configLocation);
  }

  public MyCacheManager(RedissonClient redisson, String configLocation,
      Codec codec) {
    super(redisson, configLocation, codec);
  }

  @Override
  protected CacheConfig createDefaultConfig() {
    CacheConfig config = new CacheConfig(unit.toMillis(ttl), maxIdleTime);
    config.setMaxSize(this.maxSize);
    return config;
  }

  public long getTtl() {
    return ttl;
  }

  public MyCacheManager setTtl(long ttl) {
    this.ttl = ttl;
    return this;
  }

  public long getMaxIdleTime() {
    return maxIdleTime;
  }

  public MyCacheManager setMaxIdleTime(long maxIdleTime) {
    this.maxIdleTime = maxIdleTime;
    return this;
  }

  public int getMaxSize() {
    return maxSize;
  }

  public MyCacheManager setMaxSize(int maxSize) {
    this.maxSize = maxSize;
    return this;
  }

  public TimeUnit getUnit() {
    return unit;
  }

  public MyCacheManager setUnit(TimeUnit unit) {
    this.unit = unit;
    return this;
  }
}

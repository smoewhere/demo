package org.fan.demo.cachedemo.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.fan.demo.cachedemo.config.cache.MyCacheManager;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author Fan
 * @version 1.0
 * @date 2021.1.2 16:05
 */
@ConditionalOnProperty(value = "spring.cache.enable")
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
@EnableCaching
public class CacheableConfig {

  private final RedisProperties properties;

  public CacheableConfig(RedisProperties properties) {
    this.properties = properties;
  }

  @Bean("cacheManager")
  @Primary
  CacheManager myCacheManager() {
    Config config = createConfig();
    config.setCodec(new JsonJacksonCodec());
    RedissonClient client = Redisson.create(config);
    MyCacheManager cacheManager = new MyCacheManager(client);
    cacheManager.setTtl(60);
    cacheManager.setUnit(TimeUnit.SECONDS);
    cacheManager.setCodec(new JsonJacksonCodec());
    return cacheManager;
  }


  private Config createConfig() {
    Config config = null;
    if ((config = getSentinelConfig()) != null) {
      return config;
    }
    if ((config = getClusterConfiguration()) != null) {
      return config;
    }
    return getStandaloneConfig();
  }

  private Config getStandaloneConfig() {
    Config config = new Config();
    String address = "";
    if (StringUtils.isNotBlank(properties.getUrl())) {
      ConnectionInfo info = parseUrl(properties.getUrl());
      URI uri = info.getUri();
      String password = info.getPassword();
      properties.setPassword(password);
      String host = uri.getHost();
      int port = uri.getPort();
      address = createOrDefaultAddress(host, port);
    } else {
      String host = properties.getHost();
      int port = properties.getPort();
      address = createOrDefaultAddress(host, port);
    }
    SingleServerConfig singleServerConfig = config.useSingleServer().setAddress(address)
        .setDatabase(properties.getDatabase())
        .setPassword(properties.getPassword()).setDnsMonitoringInterval(-1);
    if (properties.getJedis().getPool() != null) {
      singleServerConfig.setConnectionPoolSize(properties.getJedis().getPool().getMaxActive());
    }
    return config;
  }

  private Config getSentinelConfig() {
    RedisProperties.Sentinel sentinel = properties.getSentinel();
    if (sentinel == null) {
      return null;
    }
    Config config = new Config();
    String passwd = Optional.ofNullable(sentinel.getPassword()).orElseGet(properties::getPassword);
    config.useSentinelServers().setPassword(passwd).setMasterName(sentinel.getMaster())
        .setDatabase(properties.getDatabase()).setDnsMonitoringInterval(-1L)
        .addSentinelAddress(sentinel.getNodes().toArray(new String[0]));
    return config;
  }

  private Config getClusterConfiguration() {
    RedisProperties.Cluster cluster = properties.getCluster();
    if (cluster == null) {
      return null;
    }
    List<String> nodes = cluster.getNodes();
    Config config = new Config();
    config.useClusterServers().setPassword(properties.getPassword())
        .addNodeAddress(nodes.toArray(new String[0]))
        .setRetryAttempts(Optional.ofNullable(cluster.getMaxRedirects()).orElse(3))
        .setDnsMonitoringInterval(-1);
    return config;
  }

  private ConnectionInfo parseUrl(String url) {
    try {
      URI uri = new URI(url);
      String scheme = uri.getScheme();
      if (!"redis".equals(scheme) && !"rediss".equals(scheme)) {
        throw new IllegalArgumentException(url);
      }
      String password = null;
      if (uri.getUserInfo() != null) {
        password = uri.getUserInfo();
        int index = password.indexOf(':');
        if (index >= 0) {
          password = password.substring(index + 1);
        }
      }
      return new ConnectionInfo(uri, password);
    } catch (URISyntaxException ex) {
      throw new IllegalArgumentException(url, ex);
    }
  }

  private String createOrDefaultAddress(String host, int port) {
    if (StringUtils.isBlank(host)) {
      host = "127.0.0.1";
    }
    if (port == 0) {
      port = 6379;
    }
    return String.format("redis://%s:%d", host, port);
  }

  static class ConnectionInfo {

    private final URI uri;
    private final String password;

    public ConnectionInfo(URI uri, String password) {
      this.uri = uri;
      this.password = password;
    }

    public URI getUri() {
      return this.uri;
    }

    public String getPassword() {
      return this.password;
    }
  }

}

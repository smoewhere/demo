package org.fan.demo.components;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.config.ConfigData;
import org.springframework.boot.context.config.ConfigDataLoader;
import org.springframework.boot.context.config.ConfigDataLoaderContext;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.core.env.MapPropertySource;

/**
 * @author Fan
 * @version 1.0
 * @date 2022/8/29 17:37
 */
@Slf4j
public class FanConfigDataLoader implements ConfigDataLoader<FanConfigDataResource> {

  @Override
  public boolean isLoadable(ConfigDataLoaderContext context, FanConfigDataResource resource) {
    return ConfigDataLoader.super.isLoadable(context, resource);
  }

  @Override
  public ConfigData load(ConfigDataLoaderContext context, FanConfigDataResource resource)
      throws IOException, ConfigDataResourceNotFoundException {
    Map<String, Object> map = new HashMap<>();
    map.put("org.fan.test.age", 18);
    MapPropertySource source = new MapPropertySource("fan_gen", map);
    return new ConfigData(Collections.singletonList(source));
  }
}

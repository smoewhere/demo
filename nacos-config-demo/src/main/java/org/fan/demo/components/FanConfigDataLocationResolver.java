package org.fan.demo.components;

import java.util.Collections;
import java.util.List;
import org.springframework.boot.context.config.ConfigDataLocation;
import org.springframework.boot.context.config.ConfigDataLocationNotFoundException;
import org.springframework.boot.context.config.ConfigDataLocationResolver;
import org.springframework.boot.context.config.ConfigDataLocationResolverContext;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.boot.context.config.Profiles;

/**
 *
 *
 * @author Fan
 * @version 1.0
 * @date 2022/8/29 17:37
 */
public class FanConfigDataLocationResolver implements ConfigDataLocationResolver<FanConfigDataResource> {

  @Override
  public boolean isResolvable(ConfigDataLocationResolverContext context, ConfigDataLocation location) {
    if (location.isOptional()) {
      return false;
    }
    return location.hasPrefix("fann");
  }

  @Override
  public List<FanConfigDataResource> resolve(ConfigDataLocationResolverContext context, ConfigDataLocation location)
      throws ConfigDataLocationNotFoundException, ConfigDataResourceNotFoundException {
    FanConfigDataResource resource = new FanConfigDataResource();
    return Collections.singletonList(resource);
  }

  @Override
  public List<FanConfigDataResource> resolveProfileSpecific(ConfigDataLocationResolverContext context,
      ConfigDataLocation location, Profiles profiles) throws ConfigDataLocationNotFoundException {
    return ConfigDataLocationResolver.super.resolveProfileSpecific(context, location, profiles);
  }
}

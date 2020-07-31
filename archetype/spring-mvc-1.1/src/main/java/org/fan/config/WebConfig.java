package org.fan.config;

import java.util.ArrayList;
import java.util.List;
import org.fan.config.resolver.MyParameterResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.7.31 10:44
 * 用于配置mvc，
 */
@Component
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(new MyParameterResolver());
  }
}

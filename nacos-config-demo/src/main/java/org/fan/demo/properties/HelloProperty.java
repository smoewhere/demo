package org.fan.demo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author Fan
 * @version 1.0
 * @date 2022/8/29 16:33
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "org.fan.test")
public class HelloProperty {

  private String name;

  private Integer age;

}

package org.fan.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.7.31 10:15
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "org.fan")
public class RootConfig {

}

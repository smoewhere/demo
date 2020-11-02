package org.fan.teat.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootApplication
@EnableCaching
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ServletComponentScan(basePackages = {"org.fan.teat.security.config.servlet"})
public class SecurityApplication {

  public static void main(String[] args) {
    SecurityContextHolder.setStrategyName("");
    SpringApplication.run(SecurityApplication.class, args);
  }

}

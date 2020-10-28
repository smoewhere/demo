package org.fan.teat.security.config.security;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.10.18 20:21
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Resource
  private UserConfig userConfig;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userConfig).passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
        //.formLogin().loginProcessingUrl("/login").loginPage("/loginPage").permitAll().and()
        .authorizeRequests()
        .antMatchers("/index.html", "/css/**", "/img/**", "/js/**", "/layui/**", "/login").permitAll()
        .anyRequest().authenticated().and()
        .apply(new CustomerConfigurer<>()).and()
        .exceptionHandling().authenticationEntryPoint(new JsonAuthenticationEntryPoint()).and()
        /*// 添加自定义的session管理器。配置登录成功之后的session逻辑
        .sessionManagement().sessionAuthenticationStrategy(new SessionAuthenticationStrategy() {
      @Override
      public void onAuthentication(Authentication authentication, HttpServletRequest request,
          HttpServletResponse response) throws SessionAuthenticationException {

      }
    })*/;
  }
}

package org.fan.teat.security.config.security.configurer;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.fan.teat.security.config.security.filter.CustomerAuthenticationFilter;
import org.fan.teat.security.config.security.handler.JsonAuthenticationFailHandler;
import org.fan.teat.security.config.security.handler.JsonAuthenticationSuccessHandler;
import org.fan.teat.security.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.10.19 21:35
 */
public class CustomerConfigurer<H extends HttpSecurityBuilder<H>> extends
    AbstractAuthenticationFilterConfigurer<H, CustomerConfigurer<H>, CustomerAuthenticationFilter> {

  private static final Logger log = LoggerFactory.getLogger(CustomerConfigurer.class);

  public CustomerConfigurer() {
    super(new CustomerAuthenticationFilter(), null);

  }

  @Override
  protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
    return new AntPathRequestMatcher(loginProcessingUrl, "POST");
  }

  private void defaultConfig(H http) {
    successHandler(new JsonAuthenticationSuccessHandler());
    failureHandler(new JsonAuthenticationFailHandler());
    loginProcessingUrl("/doLogin");
    http.setSharedObject(SessionAuthenticationStrategy.class, postProcess(new RedisSessionAuthenticationStrategy()));
  }

  @Override
  public void init(H http) throws Exception {
    super.init(http);
    defaultConfig(http);
  }

  @Override
  public void configure(H http) throws Exception {
    super.configure(http);
    SecurityContextRepository securityContextRepository = http.
        getSharedObject(SecurityContextRepository.class);
    //postProcess(securityContextRepository);
  }

  private static class RedisSessionAuthenticationStrategy implements SessionAuthenticationStrategy {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request,
        HttpServletResponse response) throws SessionAuthenticationException {
      if (authentication == null || !authentication.isAuthenticated()) {
        return;
      }
      String uuid = UUID.randomUUID().toString();
      String userDetail;
      try {
        userDetail = JsonUtil.toJsonStr(authentication);
      } catch (Exception e) {
        log.error("json 解析错误", e);
        return;
      }
      String key = "session_" + uuid;
      stringRedisTemplate.opsForValue().set(key, userDetail, 1800, TimeUnit.SECONDS);
      request.setAttribute("isLogin", true);
      request.setAttribute("loginUid", uuid);
      Cookie cookie = new Cookie("UID", uuid);
      cookie.setPath("/");
      cookie.setMaxAge(60);
      response.addCookie(cookie);
    }
  }
}

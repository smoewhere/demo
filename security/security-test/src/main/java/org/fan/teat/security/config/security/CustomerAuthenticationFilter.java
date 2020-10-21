package org.fan.teat.security.config.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.10.19 21:20
 */
public class CustomerAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private static final Logger log = LoggerFactory.getLogger(CustomerAuthenticationFilter.class);

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException {
    log.info("{}", this.eventPublisher);
    return super.attemptAuthentication(request, response);
  }

  @Override
  protected String obtainPassword(HttpServletRequest request) {
    return super.obtainPassword(request);
  }
}

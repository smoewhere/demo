package org.fan.teat.security.config.servlet.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import org.fan.teat.security.config.servlet.component.RepeatAbleRequestWrapper;
import org.springframework.core.annotation.Order;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.10.28 13:24
 */
@Order(1)
@WebFilter(filterName = "requestWrapperFilter", urlPatterns = "/*")
public class RequestWrapperFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    ServletRequest requestWrapper = null;
    if (request instanceof HttpServletRequest) {
      requestWrapper = new RepeatAbleRequestWrapper((HttpServletRequest) request);
    }
    if (requestWrapper == null) {
      chain.doFilter(request, response);
    } else {
      chain.doFilter(requestWrapper, response);
    }
  }
}

package org.fan.filter;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import org.fan.config.wrapper.RepeatAbleRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.8.28 13:57
 */

@WebFilter(filterName = "baseFilter", urlPatterns = "/*")
public class BaseFilter implements Filter {

  private static final Logger log = LoggerFactory.getLogger(BaseFilter.class);

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    Enumeration<String> names = filterConfig.getInitParameterNames();
    while (names.hasMoreElements()) {
      String s = names.nextElement();
      log.info("key is {}, value is {}", s, filterConfig.getInitParameter(s));
    }
  }

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

  @Override
  public void destroy() {
    log.info("doFilter destroy!");
  }
}

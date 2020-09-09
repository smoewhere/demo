package org.fan.local.config.intercept;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.fan.local.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.9.9 18:40
 */
public class RequestCountIntercept implements HandlerInterceptor {

  private static final Logger log = LoggerFactory.getLogger(RequestCountIntercept.class);

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    log.info("[RequestCountIntercept.preHandle] start");
    String uri = request.getRequestURI();
    String host = request.getRemoteHost();
    String key = String.join("_", uri, host);
    log.info("[RequestCountIntercept.preHandle] key is {}", key);
    if (RedisUtil.setNx(key, "1")) {
      RedisUtil.expire(key, 60);
      return true;
    }
    Long incr = RedisUtil.incr(key, 1);
    if (incr > 20) {
      response.getWriter().println("1分钟之内只能访问20次！");
      return false;
    }
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    log.info("[RequestCountIntercept.postHandle] start");
  }
}

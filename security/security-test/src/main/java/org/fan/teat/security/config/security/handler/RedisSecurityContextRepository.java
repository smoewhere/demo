package org.fan.teat.security.config.security.handler;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.fan.teat.security.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.10.30 15:40
 */
@Component
public class RedisSecurityContextRepository implements SecurityContextRepository {

  private static final String COOKIE_NAME = "UID";
  private static final String PREFIX_NAME = "session_";

  private static final Logger log = LoggerFactory.getLogger(RedisSecurityContextRepository.class);

  @Resource
  private StringRedisTemplate stringRedisTemplate;

  @Override
  public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
    String uid = null;
    if ((uid = getUidFromRequest(requestResponseHolder.getRequest())) == null) {
      return SecurityContextHolder.createEmptyContext();
    }
    Cookie cookie = new Cookie("UID", uid);
    cookie.setPath("/");
    requestResponseHolder.getResponse().addCookie(cookie);
    return getContextFromRedis(getRepositoryKey(uid));
  }

  @Override
  public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
    String uuid = null;
    Boolean isLogin = (Boolean) request.getAttribute("isLogin");
    if (isLogin) {
      uuid = (String) request.getAttribute("loginUid");
    } else {
      uuid = getUidFromRequest(request);
    }
    if (uuid == null) {
      uuid = UUID.randomUUID().toString();
    }
    Authentication authentication = context.getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return;
    }
    String key = getRepositoryKey(uuid);
    if (containsKey(key)) {
      stringRedisTemplate.expire(key, 1800, TimeUnit.SECONDS);
    }
  }

  @Override
  public boolean containsContext(HttpServletRequest request) {
    String uid = getUidFromRequest(request);
    String key = getRepositoryKey(uid);
    return containsKey(key);
  }

  private boolean containsKey(String key) {
    return Optional.ofNullable(stringRedisTemplate.hasKey(key)).orElse(false);
  }

  private String getUidFromRequest(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null || cookies.length == 0) {
      return null;
    }
    String uid = null;
    for (Cookie cookie : cookies) {
      if (COOKIE_NAME.equalsIgnoreCase(cookie.getName())) {
        uid = cookie.getValue();
        break;
      }
    }
    if (uid == null) {
      uid = request.getParameter("UID");
    }
    return uid;
  }

  private String getRepositoryKey(String uid) {
    return PREFIX_NAME + uid;
  }

  private SecurityContext getContextFromRedis(String key) {
    String s = stringRedisTemplate.opsForValue().get(key);
    SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
    if (StringUtils.isBlank(s)) {
      return emptyContext;
    }
    JsonNode node = JsonUtil.fromJson(s, JsonNode.class);
    //new UsernamePasswordAuthenticationToken()
    //emptyContext.setAuthentication();
    return null;
  }
}

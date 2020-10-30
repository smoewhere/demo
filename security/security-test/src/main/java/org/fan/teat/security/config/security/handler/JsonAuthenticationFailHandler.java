package org.fan.teat.security.config.security.handler;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.fan.teat.security.dto.ResultDto;
import org.fan.teat.security.dto.ResultDto.ResultEnum;
import org.fan.teat.security.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.10.21 19:01
 */
public class JsonAuthenticationFailHandler implements AuthenticationFailureHandler {

  private static final Logger log = LoggerFactory.getLogger(JsonAuthenticationFailHandler.class);

  private static final String DEFAULT_ENCODING = "utf-8";

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    response.setContentType("application/json");
    response.setCharacterEncoding(DEFAULT_ENCODING);
    ResultDto<Object> resultDto;
    if (exception instanceof InternalAuthenticationServiceException) {
      response.setStatus(500);
      resultDto = ResultDto.buildError(exception.getMessage())
          .setCode(ResultEnum.INTERNAL_SERVER_ERROR.getResultCode());
    } else {
      response.setStatus(403);
      resultDto = ResultDto.buildError("账号密码错误！")
          .setCode(ResultEnum.ACCESS_DENIED.getResultCode());
    }
    try (PrintWriter writer = response.getWriter()) {
      writer.println(JsonUtil.toJsonStr(resultDto));
      writer.flush();
    } catch (Exception e) {
      log.error("error:", e);
    }
  }
}

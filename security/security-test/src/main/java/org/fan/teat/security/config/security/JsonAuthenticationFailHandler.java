package org.fan.teat.security.config.security;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.fan.teat.security.dto.ResultDto;
import org.fan.teat.security.dto.ResultDto.ResultEnum;
import org.fan.teat.security.utils.JsonUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.10.21 19:01
 */
public class JsonAuthenticationFailHandler implements AuthenticationFailureHandler {

  private static final String DEFAULT_ENCODING = "utf-8";

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    response.setContentType("application/json");
    response.setCharacterEncoding(DEFAULT_ENCODING);
    response.setStatus(403);
    PrintWriter writer = response.getWriter();
    ResultDto<Object> resultDto = ResultDto.buildError("登录失败！")
        .setCode(ResultEnum.INTERNAL_SERVER_ERROR.getResultCode());
    writer.println(JsonUtil.toJsonStr(resultDto));
    writer.flush();
    writer.close();
  }
}

package org.fan.teat.security.config.security.handler;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.fan.teat.security.dto.ResultDto;
import org.fan.teat.security.utils.JsonUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.10.21 18:28
 */
public class JsonAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private static final String DEFAULT_ENCODING = "utf-8";

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    response.setContentType("application/json");
    response.setCharacterEncoding(DEFAULT_ENCODING);
    PrintWriter writer = response.getWriter();
    ResultDto<Authentication> resultDto = ResultDto.buildSuccess("登录成功！", authentication);
    writer.println(JsonUtil.toJsonStr(resultDto));
    writer.flush();
    writer.close();
  }
}

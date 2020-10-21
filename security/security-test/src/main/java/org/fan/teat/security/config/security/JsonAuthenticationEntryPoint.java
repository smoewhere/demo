package org.fan.teat.security.config.security;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.fan.teat.security.dto.ResultDto;
import org.fan.teat.security.dto.ResultDto.ResultEnum;
import org.fan.teat.security.utils.JsonUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.10.21 19:48
 */
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {
    response.setContentType("application/json; charset=UTF-8");
    PrintWriter writer = response.getWriter();
    ResultDto<Object> error = ResultDto.buildError(ResultEnum.ACCESS_DENIED.getResultCode(), false,
        ResultEnum.ACCESS_DENIED.getResultMsg(), null);
    writer.println(JsonUtil.toJsonStr(error));
    writer.flush();
    writer.close();
  }
}

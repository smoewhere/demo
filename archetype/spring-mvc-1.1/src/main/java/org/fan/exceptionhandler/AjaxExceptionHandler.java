package org.fan.exceptionhandler;

import javax.servlet.http.HttpServletRequest;
import org.fan.enums.ResultEnum;
import org.fan.exception.BusinessException;
import org.fan.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.6.16 16:55
 */
@RestControllerAdvice
public class AjaxExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(AjaxExceptionHandler.class);

  @ExceptionHandler(value = BusinessException.class)
  @ResponseBody
  public ResultVo<Object> businessExceptionHandler(HttpServletRequest request, BusinessException exception) {
    log.error("catch an error when running detail:", exception);
    return ResultVo.buildError(exception.getErrorCode(), exception.getMessage());
  }

  @ExceptionHandler(value = Exception.class)
  @ResponseBody
  public ResultVo<Object> exceptionHandler(HttpServletRequest request, Exception exception) {
    log.error("catch an error when running detail:", exception);
    return ResultVo.buildError(ResultEnum.INTERNAL_SERVER_ERROR.getResultCode(), exception.toString());
  }


}

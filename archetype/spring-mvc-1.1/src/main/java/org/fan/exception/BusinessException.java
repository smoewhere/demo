package org.fan.exception;


import org.fan.enums.ResultEnum;

/**
 * 业务异常
 *
 * @author Fan
 * @version 1.0
 * @date 2020.6.16 16:44
 */
public class BusinessException extends RuntimeException {

  private String errorMsg;

  private int errorCode;

  public BusinessException() {
    super();
  }

  public BusinessException(String message) {
    super(message);
    this.errorMsg = message;
  }

  public BusinessException(ResultEnum resultEnum) {
    super(resultEnum.getResultMsg());
    this.errorMsg = resultEnum.getResultMsg();
    this.errorCode = resultEnum.getResultCode();
  }

  public BusinessException(ResultEnum resultEnum, String message) {
    super(message);
    this.errorMsg = message;
    this.errorCode = resultEnum.getResultCode();
  }

  public BusinessException(String message, int errorCode) {
    super(message);
    this.errorMsg = message;
    this.errorCode = errorCode;
  }

  public BusinessException(String message, Throwable cause) {
    super(message, cause);
    this.errorMsg = message;
  }

  public BusinessException(Throwable cause) {
    super(cause);
  }

  protected BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  @Override
  public String getMessage() {
    return errorMsg;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }
}

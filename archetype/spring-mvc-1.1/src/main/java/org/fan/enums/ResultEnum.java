package org.fan.enums;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.6.16 17:15
 */
public enum ResultEnum {
  /**
   * 成功
   */
  SUCCESS(200, "成功!"),
  /**
   * 400错误
   */
  BODY_NOT_MATCH(400, "请求的数据格式不符!"),
  /**
   * 401错误
   */
  SIGNATURE_NOT_MATCH(401, "请求的数字签名不匹配!"),
  /**
   * 404 错误
   */
  NOT_FOUND(404, "未找到该资源!"),
  /**
   * 500错误
   */
  INTERNAL_SERVER_ERROR(500, "服务器内部错误!"),
  /**
   * 503错误
   */
  SERVER_BUSY(503, "服务器正忙，请稍后再试!");

  /**
   * 错误码
   */
  private int resultCode;

  /**
   * 错误描述
   */
  private String resultMsg;

  ResultEnum(Integer resultCode, String resultMsg) {
    this.resultCode = resultCode;
    this.resultMsg = resultMsg;
  }

  public int getResultCode() {
    return resultCode;
  }

  public String getResultMsg() {
    return resultMsg;
  }
}

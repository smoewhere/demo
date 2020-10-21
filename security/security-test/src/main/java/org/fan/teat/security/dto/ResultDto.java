package org.fan.teat.security.dto;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.10.21 18:37
 */
public class ResultDto<T> {

  /**
   * http状态码，比如200,500,301等
   */
  private int code;
  /**
   * 调用接口是否成功 true or false
   */
  private boolean success;
  /**
   * 接口返回消息
   */
  private String msg;
  /**
   * 数据
   */
  private T data;


  public ResultDto() {
  }

  public ResultDto(String msg, T data) {
    this.msg = msg;
    this.data = data;
  }

  public ResultDto(boolean success, String msg, T data) {
    this.success = success;
    this.msg = msg;
    this.data = data;
  }

  public ResultDto(int code, boolean success, String msg, T data) {
    this.code = code;
    this.success = success;
    this.msg = msg;
    this.data = data;
  }

  public static <T> ResultDto<T> newResultVo() {
    return new ResultDto<>();
  }

  public static <T> ResultDto<T> buildSuccess() {
    return buildSuccess(ResultEnum.SUCCESS.getResultCode(), true);
  }

  public static <T> ResultDto<T> buildSuccess(int code, boolean success) {
    return buildSuccess(code, success, ResultEnum.SUCCESS.getResultMsg(), null);
  }

  public static <T> ResultDto<T> buildSuccess(String msg, T data) {
    return buildSuccess(ResultEnum.SUCCESS.getResultCode(), true, msg, data);
  }

  public static <T> ResultDto<T> buildSuccess(int code, boolean success, String msg, T data) {
    return new ResultDto<>(code, success, msg, data);
  }

  public static <T> ResultDto<T> buildError(String msg, T data) {
    return new ResultDto<>(ResultEnum.INTERNAL_SERVER_ERROR.getResultCode(), false, msg, data);
  }

  public static <T> ResultDto<T> buildError(String msg) {
    return buildError(msg, null);
  }

  public static <T> ResultDto<T> buildError(int code, boolean success, String msg, T data) {
    return new ResultDto<>(code, success, msg, data);
  }

  public int getCode() {
    return code;
  }

  public ResultDto<T> setCode(int code) {
    this.code = code;
    return this;
  }

  public boolean isSuccess() {
    return success;
  }

  public ResultDto<T> setSuccess(boolean success) {
    this.success = success;
    return this;
  }

  public String getMsg() {
    return msg;
  }

  public ResultDto<T> setMsg(String msg) {
    this.msg = msg;
    return this;
  }

  public T getData() {
    return data;
  }

  public ResultDto<T> setData(T data) {
    this.data = data;
    return this;
  }

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
    private final int resultCode;

    /**
     * 错误描述
     */
    private final String resultMsg;

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
}

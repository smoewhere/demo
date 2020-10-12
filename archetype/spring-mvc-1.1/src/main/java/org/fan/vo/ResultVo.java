package org.fan.vo;

import org.fan.enums.ResultEnum;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.10.12 15:07
 */
public class ResultVo<T> {

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

  public ResultVo() {
    this(200);
  }

  public ResultVo(int code) {
    this(code, true);
  }

  public ResultVo(int code, boolean success) {
    this(code, success, "");
  }

  public ResultVo(int code, boolean success, String msg) {
    this(code, success, msg, null);
  }

  public ResultVo(int code, boolean success, String msg, T data) {
    this.code = code;
    this.success = success;
    this.msg = msg;
    this.data = data;
  }

  public int getCode() {
    return code;
  }

  public ResultVo<T> setCode(int code) {
    this.code = code;
    return this;
  }

  public boolean getSuccess() {
    return success;
  }

  public ResultVo<T> setSuccess(Boolean success) {
    this.success = success;
    return this;
  }

  public String getMsg() {
    return msg;
  }

  public ResultVo<T> setMsg(String msg) {
    this.msg = msg;
    return this;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public static <T> ResultVo<T> buildSuccess() {
    return new ResultVo<>();
  }

  public static <T> ResultVo<T> buildSuccess(int code, String msg, boolean success, T data) {
    return new ResultVo<>(code, success, msg, data);
  }


  public static <T> ResultVo<T> buildSuccess(T data) {
    return buildSuccess(ResultEnum.SUCCESS.getResultCode(),
        ResultEnum.SUCCESS.getResultMsg(), true, data);
  }

  public static <T> ResultVo<T> buildError(ResultEnum errorInfo) {
    return new ResultVo<>(errorInfo.getResultCode(), false, errorInfo.getResultMsg());
  }

  public static <T> ResultVo<T> buildError(int code, String message) {
    return new ResultVo<>(code, false, message);
  }

  public static <T> ResultVo<T> buildError(String message) {
    return new ResultVo<>(ResultEnum.INTERNAL_SERVER_ERROR.getResultCode(), false, message);
  }

}

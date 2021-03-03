package org.fan.demo;

import io.lettuce.core.protocol.ProtocolKeyword;
import java.nio.charset.StandardCharsets;

/**
 * @version 1.0
 * @author: Fan
 * @date 2021/3/3 13:57
 */
public enum OtherCommandType implements ProtocolKeyword {
  /**
   * 没有定义的api
   */
  GETDEL;


  public final byte[] bytes;

  OtherCommandType() {
    this.bytes = name().getBytes(StandardCharsets.US_ASCII);
  }

  @Override
  public byte[] getBytes() {
    return bytes;
  }
}

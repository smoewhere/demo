package org.fan.teat.security;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.extern.slf4j.Slf4j;
import org.fan.teat.security.utils.SnowFlakeUtil;
import org.junit.jupiter.api.Test;

/**
 * 测试工具类的功能
 *
 * @version 1.0
 * @author: Fan
 * @date 2020.10.26 15:00
 */
@Slf4j
public class TestUtils {


  @Test
  public void TestSnowFlake() {
    for (int i = 0; i < 500; i++) {
      log.info("[TestUtils.TestSnowFlake] id = {}", SnowFlakeUtil.nextId());
    }
  }
}

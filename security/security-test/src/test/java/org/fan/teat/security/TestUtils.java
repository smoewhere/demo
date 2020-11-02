package org.fan.teat.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.stream.Collectors;
import javax.servlet.ServletInputStream;
import lombok.extern.slf4j.Slf4j;
import org.fan.teat.security.config.security.component.SysAuthenticationToken;
import org.fan.teat.security.config.security.component.SysUserDetail;
import org.fan.teat.security.utils.JsonUtil;
import org.fan.teat.security.utils.RedisUtil;
import org.fan.teat.security.utils.SnowFlakeUtil;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

/**
 * 测试工具类的功能
 *
 * @version 1.0
 * @author: Fan
 * @date 2020.10.26 15:00
 */
@Slf4j
public class TestUtils {

  private static final ObjectMapper mapper = new ObjectMapper();

  static {
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    //mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), DefaultTyping.NON_FINAL, As.PROPERTY);
  }


  @Test
  public void TestSnowFlake() {
    for (int i = 0; i < 500; i++) {
      log.info("[TestUtils.TestSnowFlake] id = {}", SnowFlakeUtil.nextId());
    }
  }

  @Test
  public void testJson() {
    Person person = new Person("name", "address");
    try {
      String value = mapper.writeValueAsString(person);
      System.out.println(value);
      JsonNode jsonNode = mapper.readValue(value, JsonNode.class);
      System.out.println(jsonNode.fieldNames());
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testClassName() {
    System.out.println(this.getClass().getName());
  }

  @Test
  public void testStream() {
    try (InputStream inputStream = Files
        .newInputStream(Paths.get("F:\\test\\test_doc\\test.txt"), StandardOpenOption.READ);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
      long start = System.currentTimeMillis();
      String reduce = reader.lines().collect(Collectors.joining("\n"));
      long end = System.currentTimeMillis();
      log.info("[TestUtils.testStream] lines is\n{}\ncost time is {}", reduce, (end - start));
    } catch (Exception e) {
      log.error("[TestUtils.testStream]", e);
    }
  }


  @Test
  public void testRedis(){

    String str = RedisUtil.getInstance().get("session_375ce598-34e0-47cb-adeb-21179dd50da1");
    SysAuthenticationToken token = JsonUtil.fromJson(str, SysAuthenticationToken.class);
    System.out.println(token);

  }
  
  @Test
  public void testMath(){
    System.out.println((float) 1 / 100);
  }


  static class Person {

    private String name;
    private String address;

    public Person() {
    }

    public Person(String name, String address) {
      this.name = name;
      this.address = address;
    }

    public String getName() {
      return name;
    }

    public Person setName(String name) {
      this.name = name;
      return this;
    }

    public String getAddress() {
      return address;
    }

    public Person setAddress(String address) {
      this.address = address;
      return this;
    }

    @Override
    public String toString() {
      return "Person{" +
          "name='" + name + '\'' +
          ", address='" + address + '\'' +
          '}';
    }
  }
}

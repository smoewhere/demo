package org.fan.demo.security.core.dto;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.10.20 23:13
 */
public class User {

  public interface UserSimple {

  }

  public interface UserDetail extends UserSimple {

  }

  @JsonView(UserSimple.class)
  private String name;
  @JsonView(UserSimple.class)
  private Integer age;
  @JsonView(UserSimple.class)
  private String address;
  @JsonView(UserDetail.class)
  private String password;

  public String getName() {
    return name;
  }

  public User setName(String name) {
    this.name = name;
    return this;
  }

  public Integer getAge() {
    return age;
  }

  public User setAge(Integer age) {
    this.age = age;
    return this;
  }

  public String getAddress() {
    return address;
  }

  public User setAddress(String address) {
    this.address = address;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public User setPassword(String password) {
    this.password = password;
    return this;
  }
}

package org.fan.teat.security.vo;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.10.22 16:08
 */
public class UserVo {

  private Integer id;
  private String name;
  private String address;
  private String email;
  private Integer age;

  public UserVo() {
  }

  public UserVo(Integer id, String name, String address, String email, Integer age) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.email = email;
    this.age = age;
  }

  public Integer getId() {
    return id;
  }

  public UserVo setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public UserVo setName(String name) {
    this.name = name;
    return this;
  }

  public String getAddress() {
    return address;
  }

  public UserVo setAddress(String address) {
    this.address = address;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public UserVo setEmail(String email) {
    this.email = email;
    return this;
  }

  public Integer getAge() {
    return age;
  }

  public UserVo setAge(Integer age) {
    this.age = age;
    return this;
  }
}

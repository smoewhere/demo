package org.fan.demo.common.model;

import java.io.Serializable;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.11.8 2:07
 */
public class User implements Serializable {

  private String name;
  private String address;

  public String getName() {
    return name;
  }

  public User setName(String name) {
    this.name = name;
    return this;
  }

  public String getAddress() {
    return address;
  }

  public User setAddress(String address) {
    this.address = address;
    return this;
  }
}

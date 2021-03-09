package org.fan.demo.entity;

/**
 * @version 1.0
 * @author: Fan
 * @date 2021/3/8 19:17
 */
public class Cat {

  private String name;

  private String sex;

  private Integer age;

  private int agePri;

  public String getName() {
    return this.name;
  }

  public Cat setName(String name) {
    this.name = name;
    return this;
  }

  public String getSex() {
    return this.sex;
  }

  public Cat setSex(String sex) {
    this.sex = sex;
    return this;
  }

  public Integer getAge() {
    return this.age;
  }

  public Cat setAge(Integer age) {
    this.age = age;
    return this;
  }

  public int getAgePri() {
    return this.agePri;
  }

  public Cat setAgePri(int agePri) {
    this.agePri = agePri;
    return this;
  }
}

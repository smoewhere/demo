package org.fan.demo.spel.model;

import java.time.LocalDateTime;

/**
 * @version 1.0
 * @author: Fan
 * @date 2021.1.25 18:51
 */
public class Person {

  private String name;

  private LocalDateTime birthday;

  private Integer age;

  public Person() {
  }

  public Person(String name, LocalDateTime birthday, Integer age) {
    this.name = name;
    this.birthday = birthday;
    this.age = age;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDateTime getBirthday() {
    return this.birthday;
  }

  public void setBirthday(LocalDateTime birthday) {
    this.birthday = birthday;
  }

  public Integer getAge() {
    return this.age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public Boolean isMember(String name){
    return false;
  }
}

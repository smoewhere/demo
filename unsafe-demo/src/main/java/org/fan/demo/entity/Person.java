package org.fan.demo.entity;

import java.util.List;
import java.util.StringJoiner;

/**
 * @version 1.0
 * @author: Fan
 * @date 2021/3/8 19:17
 */
public class Person {

  private String name;

  private List<Cat> catList;

  private String address;

  public Person() {
    System.out.println("init");
  }

  public String getName() {
    return this.name;
  }

  public Person setName(String name) {
    this.name = name;
    return this;
  }

  public List<Cat> getCatList() {
    return this.catList;
  }

  public Person setCatList(List<Cat> catList) {
    this.catList = catList;
    return this;
  }

  public String getAddress() {
    return this.address;
  }

  public Person setAddress(String address) {
    this.address = address;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Person.class.getSimpleName() + "[", "]")
        .add("name='" + name + "'")
        .add("catList=" + catList)
        .add("address='" + address + "'")
        .toString();
  }
}

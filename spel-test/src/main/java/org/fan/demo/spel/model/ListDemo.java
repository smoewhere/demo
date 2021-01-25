package org.fan.demo.spel.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @author: Fan
 * @date 2021.1.25 19:10
 */
public class ListDemo {

  public List<Boolean> booleanList = new ArrayList<>();

  public List<String> stringList;

  @Override
  public String toString() {
    return "ListDemo{" +
        "booleanList=" + booleanList +
        ", stringList=" + stringList +
        '}';
  }
}

package org.fan.demo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 */
public class App {

  private static final Logger log = LoggerFactory.getLogger(App.class);

  public static void main(String[] args) {
    System.out.println("Hello World!");
    log.info("[App.main] main");
  }
}

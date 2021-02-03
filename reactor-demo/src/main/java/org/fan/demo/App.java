package org.fan.demo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 */
public class App {

  public static void main(String[] args) {
    AtomicLong i = new AtomicLong();
    while (true) {
      try {
        TimeUnit.SECONDS.sleep(5);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println(i.incrementAndGet());
    }
  }
}

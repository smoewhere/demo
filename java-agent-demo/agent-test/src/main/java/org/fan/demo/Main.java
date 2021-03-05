package org.fan.demo;

import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version 1.0
 * @author: Fan
 * @date 2021/3/1 09:59
 */
public class Main {

  private static final Logger log = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    log.info("[Main.main] start");
    Main main = new Main();
    while (true) {
      System.out.println(main.getInt());
      try {
        TimeUnit.SECONDS.sleep(2);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private int getInt(){
    return 100;
  }

}

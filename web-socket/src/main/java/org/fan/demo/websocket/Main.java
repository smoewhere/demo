package org.fan.demo.websocket;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Fan
 * @version 1.0
 * @date 2021.1.12 22:10
 */
public class Main {

  private static long delay = TimeUnit.SECONDS.toNanos(10);
  private static int count = 5;
  private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

  public static void main(String[] args) {
    ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
    ScheduledFuture<?> schedule = service
        .schedule(() -> System.out.println("sss"), 2L, TimeUnit.SECONDS);
    service.shutdown();
    System.out.println(1);
  }

  private static class IdleTask implements Runnable{

    private boolean isPing;
    private long lastPingTime = System.nanoTime();
    private int innerCount = 0;
    private ScheduledFuture<?> scheduledFuture;

    public void run() {
      long nextDelay = delay;
      if (!isPing) {
        // 当前时间减去上次时间的时间差与心跳时间间隔的差
        nextDelay -= System.nanoTime() - lastPingTime;
      }
      if (nextDelay <=0) {
        // 心跳超时
        if (innerCount++ > count) {
          //stop
          System.out.println("stop:" + System.nanoTime());
        } else {
          System.out.println("超时未到次数:" + System.nanoTime());
          scheduledFuture = scheduledExecutorService
              .schedule(this, delay, TimeUnit.NANOSECONDS);
        }
      } else {
        System.out.println("没有超时:" + System.nanoTime());
        scheduledFuture = scheduledExecutorService
            .schedule(this, nextDelay, TimeUnit.NANOSECONDS);
      }
    }
  }

}

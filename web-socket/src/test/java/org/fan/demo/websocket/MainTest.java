package org.fan.demo.websocket;

import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

/**
 * @author Fan
 * @version 1.0
 * @date 2021.1.10 3:15
 */
public class MainTest {

  private final ConcurrentLinkedQueue<String> TASK_QUEUE = new ConcurrentLinkedQueue<>();


  private final ExecutorService TASK_POOL = Executors.newFixedThreadPool(1);

  public static void main(String[] args) {
    MainTest test = new MainTest();
    test.init();
    for (int i = 0; i < 20; i++) {
      test.add("msg: " + i);
    }
  }

  public boolean add(String msg) {
    return TASK_QUEUE.add(msg);
  }

  public void init() {
    //TASK_POOL.execute(new InnerRunnable(TASK_QUEUE));
    Flux.create(fluxSink -> {
      new Thread(()->{
        while (true) {
          if (TASK_QUEUE.isEmpty()) {
            try {
              TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          } else {
            while (!TASK_QUEUE.isEmpty()) {
              String msg = TASK_QUEUE.poll();
              fluxSink.next(msg);
            }
          }
        }
      }).start();
    }).subscribe(System.out::println);
  }

  public void destroy() {
    TASK_POOL.shutdownNow();
  }

  static class InnerRunnable implements Runnable {

    private final ConcurrentLinkedQueue<String> TASK_QUEUE;

    private final ThreadPoolExecutor TASK_POOL = new ThreadPoolExecutor(50, 200, 0L,
        TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<Runnable>(1000));

    public InnerRunnable(ConcurrentLinkedQueue<String> taskQueue) {
      this.TASK_QUEUE = taskQueue;
    }

    @Override
    public void run() {
      while (true) {
        try {
          if (TASK_QUEUE.isEmpty()) {
            try {
              TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          } else {
            while (!TASK_QUEUE.isEmpty()) {
              String msg = TASK_QUEUE.poll();
              System.out.println(msg);
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

}

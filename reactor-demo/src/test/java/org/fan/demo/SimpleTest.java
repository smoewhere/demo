package org.fan.demo;


import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.FluxSink.OverflowStrategy;
import reactor.util.Loggers;

/**
 * Unit test for simple App.
 */
public class SimpleTest {

  private static final Logger log = LoggerFactory.getLogger(SimpleTest.class);

  /*@BeforeAll
  public void init(){
    ReactorDebugAgent.init();
  }*/

  /**
   * Rigorous Test :-)
   */
  @Test
  public void shouldAnswerWithTrue() {
    assertTrue(true);
  }

  /**
   * 从Iterable构建的流
   */
  @Test
  public void testFluxIterable() {
    List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
    Disposable disposable = Flux.fromIterable(list)
        .doOnError(throwable -> log.error("[SimpleTest.testFlux]", throwable))
        .doOnNext(System.out::println).reduce(Integer::sum).subscribe(System.out::println);
  }

  @Test
  public void testFluxConcatWithValues() {
    List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
    Flux.fromIterable(list).doOnNext(i -> System.out.println("x:" + i))
        .doOnRequest(request -> System.out.println("request:" + request))
        .concatWithValues(9, 10)
        .subscribe(System.out::println);
  }

  /**
   * 由create创建的flux，通过FluxSink的next方法提供元素，通过OverflowStrategy设置背压模式
   */
  @Test
  public void testFluxCreateSync() {
    Flux.create(this::applySync, OverflowStrategy.BUFFER).log(Loggers.getLogger(SimpleTest.class))
        .subscribe(System.out::println);
    System.out.println("111");
  }

  @Test
  public void testFluxCreateAsync() {
    Integer last = Flux.create(this::applyAsync).log(Loggers.getLogger(SimpleTest.class))
        .doOnNext(System.out::println)
        .blockLast();
    System.out.println(last);
  }

  @Test
  public void testFluxPush(){
    Flux.push(this::applySync).log(Loggers.getLogger(SimpleTest.class))
        .subscribe(System.out::println);
  }

  @Test
  public void testCompletableFuture() {
    CompletableFuture<String> future = CompletableFuture.supplyAsync(this::getX)
        .thenApply(s -> s + "bb");
    try {
      String s = future.get();
      System.out.println(s);
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }


  private void applySync(FluxSink<Integer> fluxSink) {
    fluxSink.onDispose(()-> System.out.println("end"));
    for (int i = 0; i < 10; i++) {
      try {
        TimeUnit.MILLISECONDS.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      fluxSink.next(i);
    }
  }

  private void applyAsync(FluxSink<Integer> fluxSink) {
    fluxSink.onDispose(()-> System.out.println("end"));
    new Thread(()->{
      for (int i = 0; i < 10; i++) {
        try {
          TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        fluxSink.next(i);
      }
      fluxSink.complete();
    }).start();
  }

  private String getX() {
    return "aaaaa";
  }
}

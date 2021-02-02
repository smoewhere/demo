package org.fan.demo;

import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;

/**
 * java自带的Flow接口实现
 *
 * @version 1.0
 * @author: Fan
 * @date 2021/2/2 14:57
 */
@Slf4j
public class ReactiveStreamTest {


  public static void main(String[] args) {
    //1.创建 生产者Publisher JDK9自带的 实现了Publisher接口
    SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();

    //2.创建 订阅者 Subscriber，需要自己去实现内部方法
    Subscriber<Integer> subscriber = new MySubscribe();
    //Subscriber<Integer> subscriber2 = new MySubscribe();
    //3。发布者和订阅者 建立订阅关系 就是回调订阅者的onSubscribe方法传入订阅合同
    publisher.subscribe(subscriber);
    //publisher.subscribe(subscriber2);
    //4.发布者 生成数据
    for (int i = 1; i <= 5; i++) {
      log.info("【生产数据 {} 】", i);
      //submit是一个阻塞方法，此时会调用订阅者的onNext方法.关键在于subscription的request方法，如果
      // request方法没有要数据，是不会触发onNext方法的
      publisher.submit(i);
    }
    //5.发布者 数据都已发布完成后，关闭发送，此时会回调订阅者的onComplete方法
    publisher.close();
    //主线程睡一会
    try {
      Thread.currentThread().join(100000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static class MySubscribe implements Flow.Subscriber<Integer> {
    private static final AtomicInteger x = new AtomicInteger(0);
    private Flow.Subscription subscription;
    private final String name;

    public MySubscribe() {
      this.name = "sub" + x.getAndIncrement();
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
      this.subscription = subscription;
      System.out.println(name + "订阅成功。。");
      subscription.request(1);
      System.out.println(name + "订阅方法里请求一个数据");
    }
    @Override
    public void onNext(Integer item) {
      log.info("{}【onNext 接受到数据 item : {}】 ", name ,item);
      //subscription.request(1);
    }
    @Override
    public void onError(Throwable throwable) {
      log.info("{}【onError 出现异常】", name);
      subscription.cancel();
    }
    @Override
    public void onComplete() {
      log.info("{}【onComplete 所有数据接收完成】", name);
    }
  }

}

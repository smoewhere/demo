package org.fan.demo;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @version 1.0
 * @author: Fan
 * @date 2021/2/2 16:05
 */
@Slf4j
public class ProcessorDemo {

  public static void main(String[] args) throws InterruptedException {
    //创建发布者
    SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();
    //创建 Processor 即是发布者也是订阅者
    MyProcessor myProcessor = new MyProcessor();
    //创建最终订阅者
    Flow.Subscriber<Integer> subscriber = new Flow.Subscriber<>() {
      private Flow.Subscription subscription;
      @Override
      public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1);
      }
      @Override
      public void onNext(Integer item) {
        log.info("【onNext 从Processor 接受到过滤后的 数据 item : {}】 ", item);
        this.subscription.request(1);
      }
      @Override
      public void onError(Throwable throwable) {
        log.info("【onError 出现异常】");
        subscription.cancel();
      }
      @Override
      public void onComplete() {
        log.info("【onComplete 所有数据接收完成】");
      }
    };
    //建立关系 发布者和处理器， 此时处理器扮演 订阅者
    publisher.subscribe(myProcessor);
    //建立关系 处理器和订阅者  此时处理器扮演 发布者
    myProcessor.subscribe(subscriber);
    //发布者发布数据
    publisher.submit(1);
    publisher.submit(2);
    publisher.submit(3);
    publisher.submit(4);
    publisher.close();
    TimeUnit.SECONDS.sleep(2);
  }

}

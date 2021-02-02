package org.fan.demo;

import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义的Processor
 *
 * @version 1.0
 * @author: Fan
 * @date 2021/2/2 16:01
 */
@Slf4j
public class MyProcessor extends SubmissionPublisher<Integer> implements
    Flow.Processor<Integer, Integer> {

  private Flow.Subscription subscription;

  @Override
  public void onSubscribe(Subscription subscription) {
    log.info("【Processor 收到订阅请求】");
    //保存订阅关系，需要用它来给发布者 相应
    this.subscription = subscription;
    this.subscription.request(1);
  }

  @Override
  public void onNext(Integer item) {
    log.info("【onNext 收到发布者数据  : {} 】", item);
    //做业务处理。。
    if (item % 2 == 0) {
      //筛选偶数 发送给 订阅者
      this.submit(item);
    }
    this.subscription.request(1);
  }

  @Override
  public void onError(Throwable throwable) {
    // 我们可以告诉发布者, 后面不接受数据了
    this.subscription.cancel();
  }

  @Override
  public void onComplete() {
    log.info("【处理器处理完毕】");
    this.close();
  }
}

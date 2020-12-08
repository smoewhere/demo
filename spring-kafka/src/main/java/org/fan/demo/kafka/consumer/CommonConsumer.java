package org.fan.demo.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.12.8 14:45
 */
@Slf4j
@Component
public class CommonConsumer {


  @KafkaListener(topics = {"topic1"}, groupId = "test-1", id = "ajdlasjdljasd")
  public void listen(String msg) {
    log.info("[CommonConsumer.listen] msg is {}", msg);
  }

}

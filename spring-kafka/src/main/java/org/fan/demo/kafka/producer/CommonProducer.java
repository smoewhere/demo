package org.fan.demo.kafka.producer;

import javax.annotation.Resource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.12.8 14:46
 */
@Component
public class CommonProducer {

  @Resource
  private KafkaTemplate<String, String> kafkaTemplate;

  public void sendMessage(String topic, String msg) {
    kafkaTemplate.send(topic, msg);
  }
}

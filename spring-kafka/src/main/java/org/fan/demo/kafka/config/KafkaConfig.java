package org.fan.demo.kafka.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * kafka配置的java config版本，使用java代码配置kafka，也可以使用配置文件配置。
 * <p>
 * springboot有默认的自动配置方法,具体配置请看：KafkaAutoConfiguration类
 * <p>
 * 两种配置方式可以都在项目中存在，但是java API方式会覆盖配置文件参数，具体原因可以看 {@code KafkaAutoConfiguration}
 *
 * @version 1.0
 * @author: Fan
 * @date 2020.12.8 14:10
 * @see org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
 */
@EnableKafka
@Configuration
public class KafkaConfig {

  /**
   * 配置消费者容器工厂,创建消费者时的配置来源，创建消费者可以看: {@code KafkaListenerAnnotationBeanPostProcessor}
   *
   * @return ConcurrentKafkaListenerContainerFactory
   * @see org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor
   */
  @Bean
  ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, String> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    return factory;
  }

  @Bean
  public ConsumerFactory<String, String> consumerFactory() {
    return new DefaultKafkaConsumerFactory<>(consumerProps());
  }

  /**
   * 消费者配置，配置含义查看 {@link ConsumerConfig} 或者查看官方文档
   *
   * @return 配置
   */
  private Map<String, Object> consumerProps() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.10.220:9092");
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-1");
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
    props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    return props;
  }

  /**
   * 生产者配置，配置含义查看 {@link ProducerConfig} 或者查看官方文档
   *
   * @return DefaultKafkaProducerFactory
   */
  private DefaultKafkaProducerFactory<String, String> senderProps() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.10.220:9092");
    props.put(ProducerConfig.RETRIES_CONFIG, 0);
    props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
    props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
    props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    return new DefaultKafkaProducerFactory<>(props);
  }

  /**
   * KafkaTemplate，spring封装的kafka生产者工具类
   *
   * @return KafkaTemplate
   */
  @Bean
  public KafkaTemplate<String, String> kafkaTemplate() {
    return new KafkaTemplate<>(senderProps());
  }

}

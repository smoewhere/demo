package org.fan.demo.websocket.config;

import org.fan.demo.websocket.handler.SpringWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author Fan
 * @version 1.0
 * @date 2021.1.10 13:43
 */
@Configuration
@EnableWebSocket
public class SpringWebSocketConfig implements WebSocketConfigurer {

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(springWebSocketHandler(),"/websocket/ser").setAllowedOrigins("*");
  }

  @Bean
  SpringWebSocketHandler springWebSocketHandler(){
    return new SpringWebSocketHandler();
  }
}

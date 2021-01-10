package org.fan.demo.websocket.config;

import org.fan.demo.websocket.component.websocket.SpringEndpointConfigure;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author Fan
 * @version 1.0
 * @date 2021.1.10 2:09
 */
//@Configuration
public class WebSocketConfig {

  @Bean
  public ServerEndpointExporter serverEndpointExporter(){
    return new ServerEndpointExporter();
  }

  @Bean
  SpringEndpointConfigure springEndpointConfigure(){
    return new SpringEndpointConfigure();
  }

}

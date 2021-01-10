package org.fan.demo.websocket.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Fan
 * @version 1.0
 * @date 2021.1.10 10:54
 */
@Component
public class WebSocketComponent {

  @Value("${websocket.name:default}")
  String name;

  public String getName(){
    return name;
  }

}

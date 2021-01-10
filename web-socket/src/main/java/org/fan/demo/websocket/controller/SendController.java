package org.fan.demo.websocket.controller;

import org.fan.demo.websocket.handler.SpringWebSocketHandler;
import org.fan.demo.websocket.handler.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Fan
 * @version 1.0
 * @date 2021.1.10 9:32
 */
@RestController
public class SendController {


  private final SpringWebSocketHandler springWebSocketHandler;


  public SendController(SpringWebSocketHandler springWebSocketHandler) {
    this.springWebSocketHandler = springWebSocketHandler;
  }

  @RequestMapping(path = "/send", method = RequestMethod.POST)
  public String sendMsg(@RequestBody String body) {
    springWebSocketHandler.send(body);
    return "success";
  }
}

package org.fan.demo.websocket.handler;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.MessageHandler;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.fan.demo.websocket.component.WebSocketComponent;
import org.fan.demo.websocket.component.websocket.SpringEndpointConfigure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Fan
 * @version 1.0
 * @date 2021.1.10 2:13
 */
@ServerEndpoint(value = "/websocket/ser"/*, configurator = SpringEndpointConfigure.class*/)
@Component
public class WebSocketHandler {

  private static final Logger log = LoggerFactory.getLogger(WebSocketHandler.class);
  /**
   * 连接集合
   */
  private static Map<String, Session> sessionMap = new ConcurrentHashMap<String, Session>();

  /*private final WebSocketComponent webSocketComponent;

  public WebSocketHandler(
      WebSocketComponent webSocketComponent) {
    this.webSocketComponent = webSocketComponent;
  }*/


  @OnOpen
  public void onOpen(Session session) {
    //log.info("[WebSocketHandler.onOpen] name is {}", webSocketComponent.getName());
    System.out.println(this);
    String sessionId = session.getId();
    Set<MessageHandler> handlers = session.getMessageHandlers();
    sessionMap.put(sessionId, session);
  }

  @OnClose
  public void onClose(Session session) {
    //从集合中删除
    sessionMap.remove(session.getId());
  }

  @OnMessage
  public void onMessage(String message, Session session) {
    System.out.println(message);
  }

  public void send(String msg) {
    for (Session value : sessionMap.values()) {
      try {
        value.getBasicRemote().sendText(msg);
      } catch (IOException e) {
        log.error("[WebSocketHandler.send]", e);
      }
    }
  }
}

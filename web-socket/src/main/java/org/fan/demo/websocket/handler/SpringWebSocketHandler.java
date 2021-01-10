package org.fan.demo.websocket.handler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.websocket.Session;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * @author Fan
 * @version 1.0
 * @date 2021.1.10 14:01
 */
public class SpringWebSocketHandler extends TextWebSocketHandler {

  private static AtomicInteger count = new AtomicInteger(0);

  private final static Map<String, WebSocketSession> SESSION_MAP = new ConcurrentHashMap<>();

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    String sessionId = session.getId();
    SESSION_MAP.put(sessionId, session);
    System.out.println("当前连接数：" + count.incrementAndGet());
    super.afterConnectionEstablished(session);
  }

  @Override
  protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
    super.handlePongMessage(session, message);
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    super.handleTransportError(session, exception);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    SESSION_MAP.remove(session.getId());
    System.out.println("当前连接数：" + count.decrementAndGet());
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    System.out.println(message.getPayload());
  }

  public void send(String msg) {
    for (WebSocketSession session : SESSION_MAP.values()) {
      try {
        session.sendMessage(new TextMessage(msg));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}

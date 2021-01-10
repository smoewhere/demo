package org.fan.demo.websocket.component.websocket;

import javax.websocket.server.ServerEndpointConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Fan
 * @version 1.0
 * @date 2021.1.10 11:13
 */
public class SpringEndpointConfigure extends ServerEndpointConfig.Configurator implements
    ApplicationContextAware {

  private static volatile BeanFactory context;

  @Override
  public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
    return context.getBean(clazz);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    SpringEndpointConfigure.context = applicationContext;
  }
}

package org.fan.demo.provider;

import java.util.concurrent.TimeUnit;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.fan.demo.provider.service.HelloService;
import org.fan.demo.provider.service.impl.HelloServiceImpl;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.11.8 0:51
 */
public class providerMain {

  public static void main(String[] args) {
    ApplicationConfig application = new ApplicationConfig();
    application.setName("xxx");

// 连接注册中心配置
    RegistryConfig registry = new RegistryConfig();
    registry.setAddress("127.0.0.1:6379");
    registry.setProtocol("redis");
    registry.setUsername("fan");
    registry.setPassword("fsj@123");

// 服务提供者协议配置
    ProtocolConfig protocol = new ProtocolConfig();
    protocol.setName("dubbo");
    protocol.setThreads(200);
    protocol.setPort(20890);

// 注意：ServiceConfig为重对象，内部封装了与注册中心的连接，以及开启服务端口

// 服务提供者暴露服务配置
    ServiceConfig<HelloService> service = new ServiceConfig<HelloService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
    service.setApplication(application);
    service.setRegistry(registry); // 多个注册中心可以用setRegistries()
    service.setProtocol(protocol); // 多个协议可以用setProtocols()
    service.setInterface(HelloService.class);
    service.setRef(new HelloServiceImpl());
    service.setVersion("1.0.0");

// 暴露及注册服务
    service.export();
    try {
      TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}

package org.fan.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.11.4 9:58
 */
@ComponentScan(basePackages = {"org.fan.test"})
@Configuration
@EnableWebFlux
public class Main {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
        Main.class);
    HttpHandler httpHandler = WebHttpHandlerBuilder.applicationContext(applicationContext).build();
    ReactorHttpHandlerAdapter handlerAdapter = new ReactorHttpHandlerAdapter(httpHandler);
    DisposableServer server = HttpServer.create()
        .handle(handlerAdapter).port(8081).bindNow();
    server.onDispose().block();
  }

}

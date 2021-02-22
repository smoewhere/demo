# 	WebFlux相关

## 介绍

webflux：webflux是spring项目下，基于reactor模式的非阻塞web服务端。

在[spring官网](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html#webflux-framework-choice "webflux")介绍中，指明了webflux和传统springmvc的区别和共同点：

<img src="./images/webflux_Applicability.png" alt="image-20210222095440384" style="zoom: 80%;" />

从图里可以看出，大部分springmvc的功能和注解webflux都可以使用，但是在一些阻塞的持久化API方面，webflux并不支持（由于webflux都是基于reactor-core的非阻塞api，传统jdbc是阻塞的）。官网原文写到：

> A simple way to evaluate an application is to check its dependencies. If you have blocking persistence APIs (JPA, JDBC) or networking APIs to use, Spring MVC is the best choice for common architectures at least. It is technically feasible with both Reactor and RxJava to perform blocking calls on a separate thread but you would not be making the most of a non-blocking web stack.

大致意思就是，如果要试用阻塞的持久层API，springmvc是通用体系内的最佳选择。如果一定要用，可以用Reactor和RxJava在单独的线程上执行阻塞API，但是这样对于非阻塞的web-stack来说，其性能不能得到充分的利用。

当然这里的不支持的说法，其实应该不是不能用这个意思。完全可以在非阻塞的逻辑中调用阻塞的api，但是由于nio的机制，并不是靠加大线程池去增加吞吐，有可能会导致耗时应用降低吞吐。

所以webflux比较适合io密集型的应用。

## 服务端的创建

### 一、`HttpHandler`

在整个webflux中，关键的是httphandler这个接口，这是处理request请求转发的关键。在spring适配中，目前有4种实现方式。[官网原文](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html#webflux-httphandler)

<img src="./images/webflux-httphandler.png" alt="image-20210222222325499" style="zoom: 80%;" />

### 二、实现

用netty实现的代码

```java
// 创建spring容器
AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Main.class);
// 创建httphandler
HttpHandler httpHandler = WebHttpHandlerBuilder.applicationContext(applicationContext).build();
// 适配为Reactor模式的API
ReactorHttpHandlerAdapter handlerAdapter = new ReactorHttpHandlerAdapter(httpHandler);
// 创建HTTPServer，并把之前的httpHandler放入，绑定端口等参数
DisposableServer server = HttpServer.create().handle(handlerAdapter).port(8081).bindNow();
server.onDispose().block();
```

## 关键流程


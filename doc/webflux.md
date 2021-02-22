# 	WebFlux相关

## 介绍

webflux：webflux是spring项目下，基于reactor模式的非阻塞web服务端。

在[spring官网](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html#webflux-framework-choice "webflux")介绍中，指明了webflux和传统springmvc的区别和共同点：

<img src="./images/webflux_Applicability.png" alt="image-20210222095440384" style="zoom: 80%;" />

从图里可以看出，大部分springmvc的功能和注解webflux都可以使用，但是在一些阻塞的持久化API方面，webflux并不支持（由于webflux都是基于reactor-core的非阻塞api，传统jdbc是阻塞的）。官网原文写到：

> A simple way to evaluate an application is to check its dependencies. If you have blocking persistence APIs (JPA, JDBC) or networking APIs to use, Spring MVC is the best choice for common architectures at least. It is technically feasible with both Reactor and RxJava to perform blocking calls on a separate thread but you would not be making the most of a non-blocking web stack.

大致意思就是，如果要试用阻塞的持久层API，springmvc是通用体系内的最佳选择。如果一定要用，可以用Reactor和RxJava在单独的线程上执行阻塞API，但是这样对于非阻塞的web-stack来说，其性能不能得到充分的利用。

## 服务端的创建




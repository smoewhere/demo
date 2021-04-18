package org.fan.demo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version 1.0
 * @author: Fan
 * @date 2021/3/1 09:59
 */
public class Main {

  private static final Logger log = LoggerFactory.getLogger(Main.class);

  private final HttpClient httpClient;

  public Main(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  private String getSync(String url) {
  HttpRequest getRequest = HttpRequest.newBuilder(URI.create(url)).GET()
        .timeout(Duration.ofSeconds(10)).build();
    CompletableFuture<String> future = httpClient.sendAsync(getRequest, BodyHandlers.ofString())
        .thenApply(HttpResponse::body);
    try {
     return future.get();
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) {
    HttpClient httpClient = HttpClient.newHttpClient();
    Main main = new Main(httpClient);
    String response = main.getSync("http://www.baidu.com/s?wd=百度");
    System.out.println(response);
    response = main.getSync("https://www.qq.com");
    System.out.println(response);
  }

}

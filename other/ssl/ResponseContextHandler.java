package org.ff.ssl;

import java.nio.charset.StandardCharsets;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.11.23 15:34
 */
public class ResponseContextHandler {

  private byte[] head;
  private byte[] body;
  private byte[] response;

  public byte[] head() {
    if (head != null) {
      return head;
    }
    String headString =
        "HTTP/1.1 200\n" +
            "Server: nginx/1.17.0\n" +
            "Date: Tue, 06 Aug 2019 03:33:50 GMT\n" +
            "Content-Type: text/html;charset=UTF-8\n" +
            "Content-Length: " + body().length + "\n" +
            "Connection: keep-alive\n" +
            "Content-Language: zh-CN\n\n";
    head = headString.getBytes();
    return head;
  }

  public byte[] body() {
    if (body != null) {
      return body;
    }
    String bodyString = "<!DOCTYPE html>\n" +
        "<html lang=\"en\">\n" +
        "<head>\n" +
        "\n" +
        "    <meta charset=\"UTF-8\">\n" +
        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" +
        "    <title>VIP控制台</title>\n" +
        "\n" +
        "\n" +
        "    <link href=\"https://image.yoootoo.com/css/admin.login.min.css\" rel=\"stylesheet\">\n" +
        "    <link rel=\"stylesheet\" href=\"https://image.yoootoo.com/css/theme.min.css\" id=\"stylesheetLight\">\n" +
        "\n" +
        "\n" +
        "</head>\n" +
        "\n" +
        "<body class=\"text-center\" style=\"background: white;\">\n" +
        "\n" +
        "<form class=\"form-signin\" action=\"/portal/main/adminLogin\" method=\"post\">\n" +
        "\n" +
        "    <img class=\"mb-4\" src=\"https://image.yoootoo.com/image/logo.png\" alt=\"\" width=\"72\"\n" +
        "         height=\"72\">\n" +
        "    <h1 class=\"header-title\">\n" +
        "         VIP 控制台\n" +
        "    </h1>\n" +
        "    <br/>\n" +
        "    <input type=\"text\" id=\"admin\" name=\"admin\" class=\"form-control\" placeholder=\"管理员账号\" required autofocus>\n"
        +
        "    <input type=\"password\" id=\"password\" name=\"password\" class=\"form-control\" placeholder=\"管理员密码\" required>\n"
        +
        "\n" +
        "    <span class=\"text-danger\">\n" +
        "                    您的会话已过期，请重新登录！\n" +
        "    </span>\n" +
        "\n" +
        "    <br/>\n" +
        "    <div class=\"col-auto my-1\">\n" +
        "        <div class=\"custom-control custom-checkbox mr-sm-2\">\n" +
        "            <input type=\"checkbox\" class=\"custom-control-input\" id=\"customControlAutosizing\">\n" +
        "            <label class=\"custom-control-label\" for=\"customControlAutosizing\"> 记住密码 </label>\n" +
        "        </div>\n" +
        "    </div>\n" +
        "    <br/>\n" +
        "\n" +
        "    <button class=\"btn btn-lg btn-primary btn-block\" type=\"submit\">立即登录</button>\n" +
        "\n" +
        "    <p class=\"mt-5 mb-3 text-muted\">&copy; 2018-2019</p>\n" +
        "\n" +
        "</form>\n" +
        "\n" +
        "\n" +
        "<script src=\"https://image.yoootoo.com/js/jquery.min.js?v=1.0.0\"></script>\n" +
        "<script src=\"https://image.yoootoo.com/js/bootstrap.bundle.min.js?v=1.0.0\"></script>\n" +
        "</body>\n" +
        "</html>\n";
    body = bodyString.getBytes(StandardCharsets.UTF_8);
    return body;
  }

  public byte[] response() {
    if (response != null) {
      return response;
    }
    //创建响应字节数组
    response = new byte[head().length + body().length];
    //将响应头复制到完整响应中
    System.arraycopy(head(), 0, response, 0, head().length);
    //将响应体复制到完整响应中
    System.arraycopy(body(), 0, response, head().length, body().length);
    return response;
  }
}

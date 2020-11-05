package org.fan.step;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.11.5 17:03
 */
public class ChangeStep {

  private static final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
      'a', 'b', 'c', 'd', 'e', 'f'};


  public static void main(String[] args) {
    CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    HttpPost httpPost = new HttpPost("https://sports.lifesense.com/sessions_service/login?version=4.5&systemType=2");
    httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
    httpPost.addHeader(HttpHeaders.USER_AGENT, "Dalvik/2.1.0 (Linux; U; Android 9; SM-G9500 Build/PPR1.180610.011)");
    HashMap<String, Object> body = new HashMap<>();
    body.put("appType", 6);
    body.put("clientId", "8e844e28db7245eb81823132464835eb");
    body.put("loginName", "13047600898");
    body.put("password", md5("fsj648272511"));
    body.put("roleType", 0);
    Gson gson = new Gson();
    System.out.println(gson.toJson(body));
    HttpEntity entity = new StringEntity(gson.toJson(body), "utf-8");
    httpPost.setEntity(entity);
    try {
      CloseableHttpResponse response = httpClient.execute(httpPost);
      System.out.println(response.getStatusLine().getStatusCode());
      JsonObject object = gson.fromJson(EntityUtils.toString(response.getEntity()), JsonObject.class);
      int code = object.get("code").getAsInt();
      if (code != 200) {
        System.out.println("密码错误！");
        return;
      }
      JsonObject data = object.get("data").getAsJsonObject();
      String token = data.get("accessToken").getAsString();
      String userId = data.get("userId").getAsString();
      updateStep(userId, token);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static String getNowTime() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    String format = now.format(formatter);
    long second = now.toInstant(OffsetDateTime.now().getOffset()).getEpochSecond();
    return String.format("%s,%s", format, second);
  }

  private static boolean updateStep(String userId, String token) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    String format = now.format(formatter);
    Instant instant = now.toInstant(OffsetDateTime.now().getOffset());
    long second = instant.getEpochSecond();
    String measurementTime = String.format("%s,%s", format, second);
    int step = 20000;
    HashMap<String, Object> body = new HashMap<>();
    ArrayList<HashMap<String, Object>> datas = new ArrayList<>();
    HashMap<String, Object> obj = new HashMap<>();
    obj.put("DataSource", 2);
    obj.put("calories", String.valueOf(step / 4));
    obj.put("deviceId", "M_NULL");
    obj.put("distance", String.valueOf(step / 3));
    obj.put("exerciseTime", 0);
    obj.put("isUpload", 0);
    obj.put("measurementTime", measurementTime);
    obj.put("step", String.valueOf(step));
    obj.put("type", 2);
    obj.put("updated", String.valueOf(second * 1000));
    obj.put("userId", userId);
    datas.add(obj);
    CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    HttpPost httpPost = new HttpPost(
        "https://sports.lifesense.com/sport_service/sport/sport/uploadMobileStepV2?version=4.5&systemType=2");
    httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
    httpPost.addHeader(HttpHeaders.USER_AGENT, "Dalvik/2.1.0 (Linux; U; Android 9; SM-G9500 Build/PPR1.180610.011)");
    httpPost.addHeader("Cookie", "accessToken=" + token);
    body.put("list", datas);
    Gson gson = new Gson();
    System.out.println(gson.toJson(body));
    HttpEntity entity = new StringEntity(gson.toJson(body), "utf-8");
    httpPost.setEntity(entity);
    try {
      CloseableHttpResponse response = httpClient.execute(httpPost);
      System.out.println(response.getStatusLine().getStatusCode());
      Header[] allHeaders = response.getAllHeaders();
      for (Header allHeader : allHeaders) {
        System.out.println(allHeader.getName() + ":" + allHeader.getValue());
      }
      System.out.println(EntityUtils.toString(entity));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  private static String md5(String str) {
    MessageDigest md5 = null;
    try {
      md5 = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    md5.update(str.getBytes(StandardCharsets.UTF_8));
    byte[] digest = md5.digest();
    int length = digest.length;
    char[] str2 = new char[length * 2];
    int k = 0;
    for (byte byte0 : digest) {
      str2[k++] = hexDigits[byte0 >>> 4 & 0xf];
      str2[k++] = hexDigits[byte0 & 0xf];
    }
    return new String(str2);
  }

}

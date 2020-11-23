package org.ff.ssl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.11.23 9:41
 */
public class Server {

  private static final String SERVER_KEY_STORE = "D:\\JavaTools\\keystore\\kserver.keystore";
  private static final String SERVER_KEY_STORE_PASSWORD = "123456";

  public static void main(String[] args) throws Exception {
    ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 50, 0L,
        TimeUnit.MICROSECONDS, new LinkedBlockingQueue<Runnable>(20));
    System.setProperty("javax.net.ssl.trustStore", SERVER_KEY_STORE);
    SSLContext context = SSLContext.getInstance("SSL");

    KeyStore ks = KeyStore.getInstance("PKCS12");
    // 证书加载
    ks.load(new FileInputStream(SERVER_KEY_STORE), SERVER_KEY_STORE_PASSWORD.toCharArray());
    KeyManagerFactory kf = KeyManagerFactory.getInstance("SunX509");
    kf.init(ks, SERVER_KEY_STORE_PASSWORD.toCharArray());

    context.init(kf.getKeyManagers(), null, null);

    ServerSocketFactory factory = context.getServerSocketFactory();
    SSLServerSocket server = (SSLServerSocket) factory.createServerSocket(8443);
    // 设置客户端不需要检验，单方面校验
    server.setNeedClientAuth(false);
    while (true) {
      Socket socket = server.accept();
      executor.execute(new Process(socket));
    }
  }

  static class Process implements Runnable {

    private final Socket socket;

    public Process(Socket socket) {
      this.socket = socket;
    }

    @Override
    public void run() {
      try (InputStream inputStream = socket.getInputStream();
          OutputStream out = socket.getOutputStream();) {
        byte[] bytes = new byte[4096];
        int len = 0;
        len = inputStream.read(bytes);
        System.out.println(new String(bytes, 0, len));
        ResponseContextHandler handler = new ResponseContextHandler();
        out.write(handler.response());
        out.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}

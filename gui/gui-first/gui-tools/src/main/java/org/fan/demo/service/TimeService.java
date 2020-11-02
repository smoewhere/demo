package org.fan.demo.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.fan.demo.controller.TimingController;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.10.26 19:41
 */
public class TimeService {

  private TimingController controller;

  private static InputStream inputStream;

  private static Player player;

  static {
    File file = Paths.get("E:\\微课\\音频MP3", "片头.mp3").toFile();
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(file);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      Runtime.getRuntime().exit(1);
    }

    BufferedInputStream stream = new BufferedInputStream(fis);
    ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
    byte[] bytes = new byte[1024];
    int len = 0;
    try {
      while ((len = stream.read(bytes)) > 0) {
        out.write(bytes, 0, len);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        stream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    if (out.size() == 0) {
      System.out.println("null");
      Runtime.getRuntime().exit(1);
    }
    inputStream = new ByteArrayInputStream(out.toByteArray());
    try {
      player = new Player(inputStream);
    } catch (JavaLayerException e) {
      e.printStackTrace();
      Runtime.getRuntime().exit(1);
    }
  }

  public TimeService(TimingController controller) {
    this.controller = controller;
  }

  public boolean isStarted(){
    return true;
  }

  public void playMusic(){
    try {
      player.play();
    } catch (JavaLayerException e) {
      e.printStackTrace();
    }
  }

  public void pauseMusic(){
    player.close();
    player.getPosition();
  }

  public void stopMusic(){
  }
}

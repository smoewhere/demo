package org.fan.demo;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.junit.Test;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.10.11 12:59
 */
public class MainTest {

  @Test
  public void test() {
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
    ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());
    try {
      Player player = new Player(inputStream);
      player.play();
    } catch (JavaLayerException e) {
      e.printStackTrace();
    }
  }
}

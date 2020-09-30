package org.fan.demo.controller;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import org.apache.commons.lang3.StringUtils;
import org.fan.demo.view.TimeView;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.9.30 9:51
 */
public class TimingController extends TimeView {

  private boolean isStart = false;
  private final List<String> stringList = new LinkedList<>();
  private static final Image start = new Image("/images/开始.jpg");
  private static final Image stop = new Image("/images/暂停.jpg");

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initView();
    initEvent();
  }

  private void initView() {
    startImage.setCursor(Cursor.HAND);
    clearImage.setCursor(Cursor.HAND);
    soundImage.setCursor(Cursor.HAND);
  }

  private void initEvent() {
    clearImage.setOnMouseClicked(event -> clear());
    startImage.setOnMouseClicked(event -> start());
    addListener();
  }

  private void addListener() {
    this.getTextHour().textProperty()
        .addListener(
            (observable, oldValue, newValue) -> validTextHour(this.getTextHour(), observable, oldValue, newValue));
    this.getTextHour().focusedProperty()
        .addListener(
            (observable, oldValue, newValue) -> focusListener(this.getTextHour(), observable, oldValue, newValue));
    this.getTextMin().textProperty()
        .addListener(
            (observable, oldValue, newValue) -> validTextField(this.getTextMin(), observable, oldValue, newValue));
    this.getTextMin().focusedProperty()
        .addListener(
            (observable, oldValue, newValue) -> focusListener(this.getTextMin(), observable, oldValue, newValue));
    this.getTextSec().textProperty()
        .addListener(
            (observable, oldValue, newValue) -> validTextField(this.getTextSec(), observable, oldValue, newValue));
    this.getTextSec().focusedProperty()
        .addListener(
            (observable, oldValue, newValue) -> focusListener(this.getTextSec(), observable, oldValue, newValue));
  }

  private void focusListener(final TextField textField, ObservableValue<? extends Boolean> observable, Boolean oldValue,
      Boolean newValue) {
    if (!newValue) {
      String text = textField.getText();
      if (StringUtils.isBlank(text)) {
        textField.setText("00");
        return;
      }
      if (text.length() < 2) {
        textField.setText("0" + text);
      }
    }
  }

  private boolean validTextField(final TextField textField, ObservableValue<? extends String> observable,
      String oldValue,
      String newValue) {
    if (StringUtils.isBlank(newValue)) {
      return false;
    }
    if (!StringUtils.isNumericSpace(newValue)) {
      textField.setText(oldValue);
      return false;
    }
    if (newValue.length() > 2) {
      textField.setText(oldValue);
      return false;
    }
    if (Integer.parseInt(newValue) > 59) {
      textField.setText(oldValue);
      return false;
    }
    if (Integer.parseInt(newValue) < 0) {
      textField.setText(String.valueOf(Math.abs(Integer.parseInt(newValue))));
      return false;
    }
    return true;
  }

  private void validTextHour(final TextField textField, ObservableValue<? extends String> observable, String oldValue,
      String newValue) {
    if (validTextField(textField, observable, oldValue, newValue)) {
      if (Integer.parseInt(newValue) > 23) {
        textField.setText(oldValue);
      }
    }

  }

  private void start() {
    startImage.setOnMouseClicked(event -> stop());
    this.startImage.setImage(stop);
    System.out.println("start");
    isStart = true;
    Thread thread = new Thread(() -> {
      while (isStart) {
        Platform.runLater(this::updateUI);
        try {
          TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      this.getTextHour().setEditable(true);
      this.getTextMin().setEditable(true);
      this.getTextSec().setEditable(true);
    });
    thread.start();
  }

  private void stop() {
    this.isStart = false;
    startImage.setOnMouseClicked(event -> start());
    startImage.setImage(start);
    String hourText = textHour.getText();
    String minText = textMin.getText();
    String secText = textSec.getText();
    if (stringList.size() > 20) {
      stringList.remove(0);
    }
    stringList.add(hourText + ":" + minText + ":" + secText);
    text1.clear();
    for (String s : stringList) {
      text1.appendText(s + "\r\n");
    }
  }

  private void clear() {
    this.isStart = false;
    startImage.setOnMouseClicked(event -> start());
    startImage.setImage(start);
    text1.clear();
    textHour.setText("00");
    textMin.setText("00");
    textSec.setText("00");
    this.getTextHour().setEditable(true);
    this.getTextMin().setEditable(true);
    this.getTextSec().setEditable(true);
  }

  private synchronized void updateUI() {
    this.getTextHour().setEditable(false);
    this.getTextMin().setEditable(false);
    this.getTextSec().setEditable(false);
    String hourStr = this.getTextHour().getText();
    if (StringUtils.isBlank(hourStr)) {
      hourStr = "0";
    }
    String minStr = this.getTextMin().getText();
    if (StringUtils.isBlank(minStr)) {
      minStr = "0";
    }
    String secStr = this.getTextSec().getText();
    if (StringUtils.isBlank(secStr)) {
      secStr = "0";
    }
    int hour = Integer.parseInt(hourStr);
    int min = Integer.parseInt(minStr);
    int sec = Integer.parseInt(secStr);
    if (sec > 0) {
      sec = sec - 1;
      if (sec < 10) {
        textSec.setText("0" + sec);
      } else {
        textSec.setText(sec + "");
      }
    } else if (min > 0) {
      min = min - 1;
      sec = 59;
      textSec.setText(59 + "");
      if (min < 10) {
        textMin.setText("0" + min);
      } else {
        textMin.setText(min + "");
      }
    } else if (hour > 0) {
      hour = hour - 1;
      min = 59;
      sec = 59;
      textSec.setText(sec + "");
      textMin.setText(min + "");
      if (hour < 10) {
        textHour.setText("0" + hour);
      } else {
        textHour.setText(hour + "");
      }
    } else {
      this.textHour.setText("00");
      this.textMin.setText("00");
      this.textSec.setText("00");
      isStart = false;
    }
  }
}

package org.fan.demo.component;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.9.23 9:56
 */
public class ClockPane extends Pane {

  private int hour;
  private int minute;
  private int second;
  private double w = 250, h = 250;

  private boolean isStop = false;

  public ClockPane() {
    setCurrentTime();
  }

  public ClockPane(int hour, int minute, int second) {
    this.hour = hour;
    this.minute = minute;
    this.second = second;
    paintClock();
  }

  public int getHour() {
    return hour;
  }

  public void setHour(int hour) {
    this.hour = hour;
    paintClock();
  }

  public int getMinute() {
    return minute;
  }

  public void setMinute(int minute) {
    this.minute = minute;
    paintClock();
  }

  public int getSecond() {
    return second;
  }

  public void setSecond(int second) {
    this.second = second;
    paintClock();
  }

  public double getW() {
    return w;
  }

  public void setW(double w) {
    this.w = w;
    paintClock();
  }

  public double getH() {
    return h;
  }

  public void setH(double h) {
    this.h = h;
    paintClock();
  }

  public void setCurrentTime() {
    Calendar calendar = new GregorianCalendar();
    this.hour = calendar.get(Calendar.HOUR_OF_DAY);
    this.minute = calendar.get(Calendar.MINUTE);
    this.second = calendar.get(Calendar.SECOND);
    paintClock();
  }

  protected void paintClock() {
    double clockRadius = Math.min(w, h) * 0.8 * 0.5;
    double centerX = w / 2;
    double centerY = h / 2;

    Circle circle = new Circle(centerX, centerY, clockRadius);
    circle.setFill(Color.WHITE);
    circle.setStroke(Color.BLACK);

    Text t1 = new Text(centerX - 5, centerY - clockRadius + 12, "12");
    Text t2 = new Text(centerX - clockRadius + 3, centerY + 5, "9");
    Text t3 = new Text(centerX + clockRadius - 10, centerY + 3, "3");
    Text t4 = new Text(centerX - 3, centerY + clockRadius - 3, "6");

    double sLength = clockRadius * 0.8;
    double scondX = centerX + sLength * Math.sin(second * (2 * Math.PI / 60));
    double scondY = centerY - sLength * Math.cos(second * (2 * Math.PI / 60));
    Line sline = new Line(centerX, centerY, scondX, scondY);
    sline.setStroke(Color.RED);

    double mLength = clockRadius * 0.65;
    double minuteX = centerX + mLength * Math.sin(minute * (2 * Math.PI / 60));
    double minuteY = centerY - mLength * Math.cos(minute * (2 * Math.PI) / 60);
    Line mline = new Line(centerX, centerY, minuteX, minuteY);
    mline.setStroke(Color.BLUE);

    double hLength = clockRadius * 0.5;
    double hourX = centerX + hLength * Math.sin((hour % 12 + minute / 60.0) * (2 * Math.PI / 12));
    double hourY = centerY - hLength * Math.cos((hour % 12 + minute / 60) * (2 * Math.PI / 12));
    Line hline = new Line(centerX, centerY, hourX, hourY);
    hline.setStroke(Color.GREEN);

    getChildren().clear();
    getChildren().addAll(circle, t1, t2, t3, t4, sline, mline, hline);

  }

  public void start() {
    Thread thread = new Thread(() -> {
      while (!isStop) {
        Platform.runLater(this::setCurrentTime);
        try {
          TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    },"JavaFX Application Thread");
    thread.start();
  }

  public void stop() {
    this.isStop = true;
  }
}

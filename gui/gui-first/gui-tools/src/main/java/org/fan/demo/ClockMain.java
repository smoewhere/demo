package org.fan.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.fan.demo.component.ClockPane;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.9.23 9:57
 */
public class ClockMain extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    ClockPane clock = new ClockPane();
    BorderPane borderPane = new BorderPane();

    borderPane.setCenter(clock);

    EventHandler<ActionEvent> eventHandler = e -> {
      clock.setCurrentTime();
      String timeString = clock.getHour() + ":" + clock.getMinute() + ":" + clock.getSecond();
      Label lblCurrentTime = new Label(timeString);
      borderPane.setCenter(clock);
      borderPane.setBottom(lblCurrentTime);
      BorderPane.setAlignment(lblCurrentTime, Pos.TOP_CENTER);
    };

    Timeline animation = new Timeline(new KeyFrame(Duration.millis(1000), eventHandler));
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.play();
    System.out.println(Thread.currentThread().getName());
    System.out.println(Thread.currentThread().getThreadGroup().getName());
    //clock.start();
    Scene scene = new Scene(borderPane, 250, 250);
    primaryStage.setTitle("ClockAnimation");
    primaryStage.setScene(scene);
    primaryStage.show();

    borderPane.widthProperty().addListener(ov ->
        clock.setW(borderPane.getWidth())
    );

    borderPane.heightProperty().addListener(ov ->
        clock.setH(borderPane.getHeight())
    );
  }

  public static void main(String[] args) {
    launch(args);
  }
}

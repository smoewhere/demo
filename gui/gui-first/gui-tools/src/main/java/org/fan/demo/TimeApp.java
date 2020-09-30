package org.fan.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.9.30 9:52
 */
public class TimeApp extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("/timing.fxml"));
    Scene scene = new Scene(root);
    scene.getStylesheets().add("css/test.css");
    primaryStage.setScene(scene);
    primaryStage.setTitle("计时器");
    primaryStage.show();
    primaryStage.setOnCloseRequest(event -> System.exit(0));
  }

  public static void main(String[] args) {
    launch(args);
  }
}

package org.fan.demo;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.fan.demo.component.JsonGroup;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.9.21 18:02
 */
public class Main2 extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    JsonGroup root = new JsonGroup();
    Scene scene = root.createJsonScene();
    primaryStage.setScene(scene);
    primaryStage.setTitle("Json转换");
    primaryStage.getIcons().add(new Image("/images/json.png"));
    primaryStage.setWidth(950);
    primaryStage.setHeight(750);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

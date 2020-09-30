package org.fan.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.fan.demo.component.JsonGroup;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.9.23 11:52
 */
public class TableMain extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    TabPane tabPane = new TabPane();
    Tab json = new Tab("json");
    json.setContent(new JsonGroup().createJsonGroup());
    tabPane.getTabs().add(json);
    Pane pane = new Pane();
    pane.getChildren().add(tabPane);
    primaryStage.setScene(new Scene(pane));
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

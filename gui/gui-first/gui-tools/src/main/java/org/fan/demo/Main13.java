package org.fan.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * 对应bilibili javaFX13课
 *
 * @author Fan
 * @version 1.0
 * @date 2020.9.26 21:31
 */
public class Main13 extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane pane = new AnchorPane();
        Button button = new Button("btn1");
        pane.getChildren().add(button);
        AnchorPane.setTopAnchor(button, 10.0);
        AnchorPane.setLeftAnchor(button, 10.0);
        AnchorPane.setRightAnchor(button, 0.0);
        AnchorPane.setBottomAnchor(button, 0.0);
        primaryStage.setScene(new Scene(pane, 800, 800));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package org.fan.demo.lesson;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * 对应bilibil 14课,水平布局和垂直布局的类HBox，VBox
 *
 * @author Fan
 * @version 1.0
 * @date 2020.10.9 22:03
 */
public class Main14 extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Button btn1 = new Button("btn1");
        Button btn2 = new Button("btn2");
        Button btn3 = new Button("btn3");
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setStyle("-fx-background-color: #FFFF33");
        HBox hBox = new HBox();
        hBox.setStyle("-fx-background-color: #33b8ff");
        hBox.getChildren().addAll(btn1, btn2, btn3);
        // 组件间距
        hBox.setSpacing(10);
        // 设置组件外边距
        HBox.setMargin(btn1,new Insets(20));
        // 设置对齐方式
        hBox.setAlignment(Pos.CENTER);
        hBox.setPrefHeight(200);
        hBox.setPrefWidth(500);
        anchorPane.getChildren().add(hBox);

        Scene scene = new Scene(anchorPane);
        primaryStage.setScene(scene);
        primaryStage.setWidth(800);
        primaryStage.setHeight(800);
        primaryStage.show();
    }
}

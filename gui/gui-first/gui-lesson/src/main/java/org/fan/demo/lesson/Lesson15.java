package org.fan.demo.lesson;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 * 对应bilibil 15课 https://space.bilibili.com/5096022/channel/detail?cid=16953
 *
 * @author Fan
 * @version 1.0
 * @date 2020.10.25 12:50
 */
public class Lesson15 extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    Button btn1 = new Button("btn1");
    Button btn2 = new Button("btn2");
    Button btn3 = new Button("btn3");
    Button btn4 = new Button("btn4");
    Button btn5 = new Button("btn5");
    Button btn6 = new Button("btn6");
    Button btn7 = new Button("btn7");
    Button btn8 = new Button("btn8");
    // 流式布局
    FlowPane flowPane = new FlowPane();
    flowPane.setStyle("-fx-background-color: #4fafaf");
    flowPane.getChildren().addAll(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8);
    // 设置填充间距
    flowPane.setPadding(new Insets(10));
    // 设置组件水平间距
    flowPane.setHgap(10);
    // 设置组件垂直间距
    flowPane.setVgap(10);
    // 设置组件对其方式
    flowPane.setAlignment(Pos.CENTER);
    Scene scene = new Scene(flowPane);
    primaryStage.setScene(scene);
    primaryStage.setWidth(800);
    primaryStage.setHeight(800);
    primaryStage.setTitle("lesson 15");
    primaryStage.show();
  }
}

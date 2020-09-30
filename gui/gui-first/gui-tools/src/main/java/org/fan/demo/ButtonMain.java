package org.fan.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.Mnemonic;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * 与按钮相关的知识点
 * @version 1.0
 * @author: Fan
 * @date 2020.9.30 14:10
 */
public class ButtonMain extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    AnchorPane anchorPane = new AnchorPane();
    Button button = new Button("btn1");
    Scene scene = new Scene(anchorPane);
    // 设置快捷键的方式
    // 第一种
    KeyCodeCombination kc1 = new KeyCodeCombination(KeyCode.W, KeyCodeCombination.CONTROL_DOWN);
    Mnemonic mnemonic1 = new Mnemonic(button, kc1);
    scene.addMnemonic(mnemonic1);
    // 第二种
    KeyCodeCombination kc2 = new KeyCodeCombination(KeyCode.W, KeyCodeCombination.CONTROL_DOWN);
    scene.getAccelerators().put(kc2, () -> System.out.println("事件"));
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

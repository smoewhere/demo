package org.fan.demo;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 初识Fx
 *
 * @author Fan
 */
public class Main extends Application {

  private static final Logger log = LoggerFactory.getLogger(Main.class);

  private static final double STAGE_WIDTH = 500.0;
  private static final double STAGE_HEIGHT = 500.0;

  @Override
  public void start(Stage primaryStage) throws Exception {
    Group root = new Group();

    ToolBar toolBar = new ToolBar();
    Button btn1 = new Button("弹窗");
    Button btn2 = new Button("按钮2");
    Button btn3 = new Button("按钮3");
    Button btn4 = new Button("按钮4");
    toolBar.getItems().addAll(btn1, btn2, btn3, btn4);
    toolBar.setPrefHeight(20);
    root.getChildren().add(toolBar);
    /*TextArea textArea = new TextArea();
    textArea.setPrefWidth(100);
    textArea.setPrefHeight(300);
    textArea.setWrapText(true);
    textArea.setLayoutX(20);
    textArea.setLayoutY(40);
    root.getChildren().add(textArea);*/

    Button btn5 = new Button();
    btn5.setText("按钮5");
    btn5.setLayoutX(100);
    btn5.setLayoutY(100);
    btn5.setFont(Font.font(20));
    btn5.setStyle(
        "-fx-background-color: linear-gradient(to right,#00fffc,#fff600);\n"
            + "-fx-background-radius: 25;\n"
            + "-fx-border-radius: 25;");
    root.getChildren().add(btn5);
    btn5.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
      if (event.getClickCount() == 2 && event.getButton().equals(MouseButton.PRIMARY)) {
        System.out.println("双击事件");
      }
    });
    Scene scene = new Scene(root);
    scene.widthProperty().addListener((observable, oldValue, newValue) -> toolBar.setPrefWidth(newValue.doubleValue()));
    toolBar.setPrefWidth(scene.getWidth());
    primaryStage.setScene(scene);
    primaryStage.initStyle(StageStyle.DECORATED);
    primaryStage.setOpacity(1); // 设置透明度
    primaryStage.setTitle("JavaFx2");
    primaryStage.getIcons().add(new Image("/images/panda.png"));
    setStageMid(primaryStage);
    primaryStage.show();
  }

  @Override
  public void stop() throws Exception {
    log.info("application stop!");
    super.stop();
  }

  public static void main(String[] args) {
    launch(args);
  }

  private void setStageMid(Stage stage) {
    Rectangle2D bounds = Screen.getPrimary().getBounds();
    double width = bounds.getWidth();
    double height = bounds.getHeight();
    log.info("screen height is {}", height);
    log.info("screen width is {}", width);
    double stageWidth = stage.getWidth();
    if (stageWidth == 0 || Double.isNaN(stageWidth)) {
      stage.setWidth(STAGE_WIDTH);
      stageWidth = STAGE_WIDTH;
    }
    double stageHeight = stage.getHeight();
    if (stageHeight == 0 || Double.isNaN(stageHeight)) {
      stage.setHeight(STAGE_HEIGHT);
      stageHeight = STAGE_HEIGHT;
    }
    stage.setX((width - stageWidth) / 2);
    stage.setY((height - stageHeight) / 2);
  }
}

package org.fan.demo;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Fan
 */
public class Main extends Application {

  private static final Logger log = LoggerFactory.getLogger(Main.class);

  private static final double STAGE_WIDTH = 500.0;
  private static final double STAGE_HEIGHT = 500.0;

  @Override
  public void start(Stage primaryStage) throws Exception {
    //primaryStage.setScene(new Scene(new Group()));
    primaryStage.initStyle(StageStyle.UNDECORATED);
    primaryStage.setOpacity(0.5); // 设置透明度
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

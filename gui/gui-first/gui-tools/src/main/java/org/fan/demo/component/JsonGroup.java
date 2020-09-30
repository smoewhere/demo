package org.fan.demo.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collection;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.9.21 17:45
 */
public class JsonGroup extends Group {

  private static final ObjectMapper mapper = new ObjectMapper();

  static {
  }

  private TextArea origin;
  private TextArea prefect;

  private Button encode = new Button("编码");
  //private Button decode = new Button("解码");

  public JsonGroup() {
    origin = new TextArea();
    prefect = new TextArea();
    getChildren().addAll(origin, prefect, encode);
  }

  public Group createJsonGroup(){
    // 创建标题
    Text originText = new Text("原始数据");
    originText.setX(20);
    originText.setY(15);
    origin.setLayoutY(50);
    origin.setLayoutX(20);
    origin.setPrefWidth(200);
    origin.setPrefHeight(600);
    origin.setWrapText(true);

    Text prefectText = new Text("转换数据");
    prefectText.setX(300);
    prefectText.setY(15);
    prefect.setLayoutX(300);
    prefect.setLayoutY(50);
    prefect.setPrefWidth(600);
    prefect.setPrefHeight(600);
    this.getChildren().addAll(originText, prefectText);

    encode.setLayoutX(240);
    encode.setLayoutY(340);
    setListener();
    return this;
  }

  public Scene createJsonScene() {
    Group group = createJsonGroup();
    return new Scene(group);
  }

  private void setListener(){
    // 编码
    encode.setOnAction(event -> {
      if(event.getSource() == encode) {
        String originText = origin.getText();
        try {
          String string = mapper.writerWithDefaultPrettyPrinter()
              .writeValueAsString(mapper.readTree(originText));
          prefect.setText(string);
        } catch (JsonProcessingException e) {
          prefect.setText(e.getMessage());
        }
      }
    });
  }

  public TextArea getOrigin() {
    return this.origin;
  }

  public JsonGroup setOrigin(TextArea origin) {
    this.origin = origin;
    return this;
  }

  public TextArea getPrefect() {
    return this.prefect;
  }

  public JsonGroup setPrefect(TextArea prefect) {
    this.prefect = prefect;
    return this;
  }
}

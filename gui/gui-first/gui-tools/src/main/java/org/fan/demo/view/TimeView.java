package org.fan.demo.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.9.30 13:27
 */
public abstract class TimeView implements Initializable {

  @FXML
  protected TextArea text1;

  @FXML
  protected TextField textMin;

  @FXML
  protected TextField textHour;

  @FXML
  protected TextField textSec;

  @FXML
  protected ImageView startImage;

  @FXML
  protected ImageView clearImage;

  @FXML
  protected ImageView soundImage;


  public TextArea getText1() {
    return text1;
  }

  public void setText1(TextArea text1) {
    this.text1 = text1;
  }

  public TextField getTextMin() {
    return textMin;
  }

  public void setTextMin(TextField textMin) {
    this.textMin = textMin;
  }

  public TextField getTextHour() {
    return textHour;
  }

  public void setTextHour(TextField textHour) {
    this.textHour = textHour;
  }

  public TextField getTextSec() {
    return textSec;
  }

  public void setTextSec(TextField textSec) {
    this.textSec = textSec;
  }

  public ImageView getStartImage() {
    return startImage;
  }

  public void setStartImage(ImageView startImage) {
    this.startImage = startImage;
  }

  public ImageView getClearImage() {
    return clearImage;
  }

  public void setClearImage(ImageView clearImage) {
    this.clearImage = clearImage;
  }

  public ImageView getSoundImage() {
    return soundImage;
  }

  public void setSoundImage(ImageView soundImage) {
    this.soundImage = soundImage;
  }
}

package org.fan.demo.service;

import org.fan.demo.controller.TimingController;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.10.26 19:41
 */
public class TimeService {

  private TimingController controller;



  public TimeService(TimingController controller) {
    this.controller = controller;
  }

  public boolean isStarted(){
    return true;
  }

  public void playMusic(){

  }

  public void pauseMusic(){}

  public void stopMusic(){}
}

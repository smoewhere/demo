package org.fan.teat.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.10.18 21:34
 */
@Controller
public class LoginController {


  @RequestMapping(path = "/loginPage")
  public String loginPage(){
    return "index";
  }

}

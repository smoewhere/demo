package org.fan.teat.security.controller;

import java.util.ArrayList;
import java.util.List;
import org.fan.teat.security.dto.ResultDto;
import org.fan.teat.security.vo.UserVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.10.22 16:06
 */
@RestController
@RequestMapping(path = "/hello")
public class HelloController {

  private final List<UserVo> userVoList = new ArrayList<>(10);

  public HelloController() {
    userVoList.add(new UserVo(1,"jack1","CA1","@163.com",18));
    userVoList.add(new UserVo(2,"jack2","CA2","@163.com",18));
    userVoList.add(new UserVo(3,"jack3","CA3","@163.com",18));
    userVoList.add(new UserVo(4,"jack4","CA4","@163.com",18));
    userVoList.add(new UserVo(5,"jack5","CA5","@163.com",18));
    userVoList.add(new UserVo(6,"jack6","CA6","@163.com",18));
    userVoList.add(new UserVo(7,"jack7","CA7","@163.com",18));
    userVoList.add(new UserVo(8,"jack8","CA8","@163.com",18));
    userVoList.add(new UserVo(9,"jack9","CA9","@163.com",18));
    userVoList.add(new UserVo(10,"jack10","CA10","@163.com",18));
  }

  @RequestMapping(value = "/get/user/{id}", method = RequestMethod.GET)
  @ResponseBody
  public ResultDto<UserVo> getUser(@PathVariable(name = "id") Integer id){
    if (id < 0 || id > 10) {
      return ResultDto.buildSuccess("用户不存在", null);
    }
    return ResultDto.buildSuccess(userVoList.get(id -1));
  }

}

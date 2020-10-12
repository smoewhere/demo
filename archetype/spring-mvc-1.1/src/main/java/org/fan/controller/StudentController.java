package org.fan.controller;

import java.util.List;
import javax.annotation.Resource;
import org.fan.entity.Student;
import org.fan.service.StudentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.10.12 14:54
 */
@RestController
@RequestMapping(value = "/student")
public class StudentController {

  @Resource
  private StudentService studentService;

  @RequestMapping(value = "/getAll", method = RequestMethod.GET)
  public List<Student> getAllStudent() {
    return studentService.getAll();
  }

}

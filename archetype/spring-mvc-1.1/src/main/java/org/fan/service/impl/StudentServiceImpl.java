package org.fan.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.fan.entity.Student;
import org.fan.mapper.StudentMapper;
import org.fan.service.StudentService;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.10.12 14:58
 */
@Service("studentService")
public class StudentServiceImpl implements StudentService {

  @Resource
  private StudentMapper studentMapper;

  @Override
  public List<Student> getAll() {
    return studentMapper.selectAll();
  }
}

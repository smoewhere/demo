package org.fan.demo.provider.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.dubbo.rpc.RpcContext;
import org.fan.demo.common.model.User;
import org.fan.demo.common.service.HelloService;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.11.8 0:56
 */
public class HelloServiceImpl implements HelloService {

  @Override
  public String hello() {
    return "HelloServiceImpl";
  }

  @Override
  public List<User> getAll() {
    Map<String, Object> attachments = RpcContext.getContext().getObjectAttachments();
    System.out.println(attachments);
    List<User> users = new ArrayList<>();
    users.add(new User().setName("jack").setAddress("address"));
    throw new RuntimeException("rururur");
  }
}

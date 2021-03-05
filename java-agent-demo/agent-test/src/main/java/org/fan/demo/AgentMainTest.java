package org.fan.demo;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version 1.0
 * @author: Fan
 * @date 2021/3/5 11:16
 */
public class AgentMainTest {

  private static final Logger log = LoggerFactory.getLogger(AgentMainTest.class);

  public static void main(String[] args) {
    log.info("[AgentMainTest.main] start");
    List<VirtualMachineDescriptor> descriptors = VirtualMachine.list();
    for (VirtualMachineDescriptor descriptor : descriptors) {
      String name = descriptor.displayName();
      log.info("[AgentMainTest.main] jvm name is {}", name);
      if (name.endsWith("org.fan.demo.Main")) {
        try {
          VirtualMachine jvm = VirtualMachine.attach(descriptor.id());
          // 包的全路径
          jvm.loadAgent("E:\\idea\\simpleWorkSpaca\\agent-main\\target\\agent-main-1.0.0.jar");
          jvm.detach();
        } catch (AttachNotSupportedException | IOException e) {
          e.printStackTrace();
        } catch (AgentLoadException e) {
          e.printStackTrace();
        } catch (AgentInitializationException e) {
          e.printStackTrace();
        }
      }
    }
    log.info("[AgentMainTest.main] end");
  }
}

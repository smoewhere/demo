#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Fan
 * @version ${version}
 * @date 2020.7.30 0:12
 */
@RestController
public class HelloController {

  @RequestMapping(value = "/hello")
  public String hello(){
    return "hello";
  }

}

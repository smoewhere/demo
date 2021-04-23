package org.fan.config;

import javax.servlet.Filter;
import javax.servlet.ServletRegistration.Dynamic;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * servlet3.0启动时会读取META-INF/services/javax.servlet.ServletContainerInitializer文件中的启动类
 * 修改继承为AbstractAnnotationConfigDispatcherServletInitializer
 *
 * 详见spring-web包
 * @version 1.1
 * @author: Fan
 * @date 2020.7.31 10:17
 *
 */
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {


  @Override
  protected Class<?>[] getRootConfigClasses() {
    return new Class[]{RootConfig.class};
  }

  @Override
  protected Class<?>[] getServletConfigClasses() {
    return new Class[0];
  }

  @Override
  protected Filter[] getServletFilters() {
    CharacterEncodingFilter filter = new CharacterEncodingFilter("UTF-8",true);
    return new Filter[]{filter};
  }

  @Override
  protected String[] getServletMappings() {
    return new String[]{"/*"};
  }

  @Override
  protected void customizeRegistration(Dynamic registration) {
  }
}

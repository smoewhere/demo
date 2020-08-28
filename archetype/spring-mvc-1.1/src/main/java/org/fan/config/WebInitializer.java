package org.fan.config;

import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.7.31 10:17
 * servlet3.0启动时会读取META-INF/services/javax.servlet.ServletContainerInitializer文件中的启动类
 * 详见spring-web包
 */
public class WebInitializer implements WebApplicationInitializer {

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    // 启动容器
    AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
    context.register(RootConfig.class);
    context.setServletContext(servletContext);
    context.refresh();
    // 添加servlet
    DispatcherServlet servlet = new DispatcherServlet(context);
    ServletRegistration.Dynamic registration = servletContext.addServlet("app", servlet);
    registration.setLoadOnStartup(1);
    registration.addMapping("/*");
    // 添加filter
    CharacterEncodingFilter filter = new CharacterEncodingFilter("UTF-8",true);
    Dynamic filterRegistration = servletContext.addFilter("encodingFilter", filter);
    filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");

  }
}

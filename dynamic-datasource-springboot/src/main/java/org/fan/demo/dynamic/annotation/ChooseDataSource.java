package org.fan.demo.dynamic.annotation;

import org.fan.demo.dynamic.config.datasource.DataSourceType;
import org.fan.demo.dynamic.enums.DataBaseType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.6.1 14:53
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ChooseDataSource {
    DataBaseType value() default DataBaseType.DS1;
}

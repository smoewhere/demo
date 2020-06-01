package org.fan.demo.dynamic.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.fan.demo.dynamic.annotation.ChooseDataSource;
import org.fan.demo.dynamic.config.datasource.DynamicDataSource;
import org.fan.demo.dynamic.enums.DataBaseType;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author Fan
 * @version 1.0
 * @date 2020.6.1 14:57
 */
@Component
@Aspect
@Slf4j
public class DynamicDataSourceAop {

    // 切入点为有ChooseDataSource注解的方法或者类
    @Pointcut(value = "@annotation(org.fan.demo.dynamic.annotation.ChooseDataSource) || @within(org.fan.demo.dynamic.annotation.ChooseDataSource)")
    //@Pointcut(value = "execution(* org.fan.demo.dynamic.service.impl.*.*(..))")
    public void pointcut() {
    }

    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("before");
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        ChooseDataSource chooseDataSource = method.getAnnotation(ChooseDataSource.class);
        if (chooseDataSource != null) {
            DynamicDataSource.setDataBaseType(chooseDataSource.value());
        } else {
            // 如果方法上没有找到，就去类上找
            Object target = joinPoint.getTarget();
            ChooseDataSource annotation = target.getClass().getAnnotation(ChooseDataSource.class);
            if (annotation == null) {
                throw new Throwable("no Annotation ChooseDataSource at type and method,but go into pointcut,maybe something is wrong");
            } else {
                DataBaseType value = annotation.value();
                DynamicDataSource.setDataBaseType(value);
            }
        }
        Object o = joinPoint.proceed();
        log.info("after");
        return o;
    }
}

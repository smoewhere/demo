package org.fan.demo.dynamic.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperProxy;
import org.apache.ibatis.binding.MapperProxyFactory;
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
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
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
@Order(0)
public class DynamicDataSourceAop {

    @Resource
    private DynamicDataSource dynamicDataSource;

    @Pointcut(value = "@annotation(org.fan.demo.dynamic.annotation.ChooseDataSource) " +
            "|| @within(org.fan.demo.dynamic.annotation.ChooseDataSource)")
    public void pointcut() {
    }

    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[DynamicDataSourceAop] before");
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        ChooseDataSource chooseDataSource = getAnnotation(joinPoint);
        DynamicDataSource.setDataBaseType(chooseDataSource.value());
        Object o = joinPoint.proceed();
        log.info("[DynamicDataSourceAop] after");
        // TransactionSynchronizationManager.unbindResourceIfPossible(dynamicDataSource);
        return o;
    }

    private ChooseDataSource getAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        // springboot 2之后默认使用的是cglib，可以获取真正方法上的注解
        ChooseDataSource chooseDataSource = null;
        chooseDataSource = method.getAnnotation(ChooseDataSource.class);
        if (chooseDataSource != null) {
            return chooseDataSource;
        } else {
            // 如果方法上没有找到，就去类上找
            Object target = joinPoint.getTarget();
            Class<?> methodDeclaringClass = method.getDeclaringClass();
            chooseDataSource = methodDeclaringClass.getAnnotation(ChooseDataSource.class);
            if (chooseDataSource == null) {
                throw new Throwable("no Annotation ChooseDataSource at type and method,but go into pointcut,maybe something is wrong");
            } else {
                return chooseDataSource;
            }
        }
    }
}

package com.example.exam.common.filter.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PointCutList {

    // Controller 용 포인트컷
    @Pointcut("execution(* com.example.exam..controller..*.*(..))")
    public void doController() {
    }

    // Service 용 포인트컷
    @Pointcut("execution(* com.example.exam..service..*.*(..))")
    public void doService() {
    }

}

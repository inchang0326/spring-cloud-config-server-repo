package com.example.exam.common.filter.aop;

import com.example.exam.biz.log.repo.mybatis.LogMapper;
import com.example.exam.common.code.EnumResultCode;
import com.example.exam.common.exception.ServiceException;
import lombok.extern.java.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

@Aspect
@Component
@Log
public class ServiceAdvice {

    @Autowired
    private LogMapper logMapper;

    private static final String logServiceFileName = "logServiceFileName";

    @Around("PointCutList.doService()")
    public Object printInputData(ProceedingJoinPoint pjp) {
        HashMap<String, Object> retMap = new HashMap<>();
        Object result = null;

        String signatureName = pjp.getSignature().getDeclaringType().getSimpleName();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        String bf = MDC.get(logServiceFileName);
        MDC.put(logServiceFileName, signatureName);

        for (int i = 0; i < method.getParameters().length; i++) {
            retMap.put(method.getParameters()[i].getName(), pjp.getArgs()[i]);
        }

        log.info(signatureName + " INPUT : " + retMap.toString());
        logging("I", signatureName, EnumResultCode.R00001.toString(), EnumResultCode.R00001.getMsg());

        try {
            result = pjp.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return result;
    }

    @AfterReturning(value = "PointCutList.doService()", returning = "result")
    public void successLog(JoinPoint joinPoint, Object result) throws RuntimeException {
        String servNm = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String retCd = EnumResultCode.R00001.toString();
        String retMsg = EnumResultCode.R00001.getMsg();
        log.info(servNm + " SUCCESS : " + "(" + retCd + ") " + retMsg);
        log.info(servNm + " OUTPUT :" + (result == null ? (String) result : result.toString()));
        logging("O", servNm, retCd, retMsg);
    }

    @AfterThrowing(value = "PointCutList.doService()", throwing = "exception")
    public void failLog(JoinPoint joinPoint, ServiceException exception) throws RuntimeException {
        String servNm = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String errCd = exception.getRtCd();
        String errMsg = exception.getMessage();
        log.info(servNm + " ERROR : " + "(" + errCd + ") " + errMsg);
        logging("O", servNm, errCd, errMsg);
    }

    private void logging(String io, String servNm, String retCd, String retMsg) {
        HashMap<String, Object> inputMap = new HashMap<>();
        inputMap.put("guid", MDC.get("GUID"));
        inputMap.put("io", io);
        inputMap.put("cstno", "0000011199017867");
        inputMap.put("sevcId", servNm);
        inputMap.put("retCd", retCd);
        inputMap.put("retMsg", retMsg);
        inputMap.put("regDt", LocalDate.now().toString().replaceAll("-", ""));
        inputMap.put("regTm", LocalTime.now().toString().replaceAll(":", "").substring(0, 6));
        logMapper.logging(inputMap);
    }
}

package com.example.exam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@ServletComponentScan // Filter 클래스 사용을 위해 선언해야 함
@EnableAspectJAutoProxy(proxyTargetClass = true) // 스프링이 요청 메소드 호출 시점을 자동으로 가로챌 수 있게 해줌(AOP)
public class ExamApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExamApplication.class, args);
    }

}

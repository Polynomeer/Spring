package com.example.firstproject.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect // AOP 클래스 선언: 부가 기능을 주입하는 클래스
@Component // IoC 컨테이너가 해당 객체를 생성 및 관리
@Slf4j
public class DebuggingAspect {

    // 대상 메서드 선택: CommentService#create()
    @Pointcut("execution(* com.example.firstproject.api.*.*(..))")
    private void cut() {
    }

    @Before("cut()")
    public void loggingArgs(JoinPoint joinPoint) { // cut()의 대상 메서드
        // 입력값 가져오기
        Object[] args = joinPoint.getArgs();

        // 클래스명
        String className = joinPoint.getTarget()
                .getClass()
                .getSimpleName();

        // 메서드명
        String methodName = joinPoint.getSignature()
                .getName();

        // 입력값 로깅하기
        // CommentService#create()의 입력값 => 5
        // CommentService#create()의 입력값 => CommentDto(id=null, ...)
        for (Object arg : args) {
            log.info("{}#{}의 입력값 => {}", className, methodName, arg);
        }
    }

    // 실행 시점 설정: cut()에 지정된 대상 호출 성공 후에 실행
    @AfterReturning(value = "cut()", returning = "returnObj")
    public void loggingReturnValue(JoinPoint joinPoint, // cut()의 대상 메서드
                                   Object returnObj) { // 반환값
        // 클래스명
        String className = joinPoint.getTarget()
                .getClass()
                .getSimpleName();

        // 메서드명
        String methodName = joinPoint.getSignature()
                .getName();

        // 반환값 로깅하기
        // CommentService#create()의 반환값 => CommentDto(id=10, ...)
        log.info("{}#{}의 반환값 => {}", className, methodName, returnObj);
    }
}

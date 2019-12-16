package com.salah.instantsystem.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class Profiler {

    private Logger LOGGER = LoggerFactory.getLogger(Profiler.class);


    @Pointcut("execution(* com.salah.instantsystem.controller.*.*(..))")
    public void logsController() { }

    /**
     * Logs between controller methods.
     *
     * @param joinPoint the join point
     * @return the object
     * @throws Throwable the throwable
     */
    @Around("logsController()")
    public Object logs(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        LOGGER.info("Start - Method called : {} -> {} with args [{}]",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                joinPoint.getArgs());

        Object output = joinPoint.proceed();

        long duration = System.currentTimeMillis() - startTime;
        LOGGER.info("End - Execution time : {} milliseconds with response [{}]", duration, output);

        return output;
    }

    /**
     * Log after throwing.
     *
     * @param joinPoint the join point
     * @param exc       the exc
     */
    @AfterThrowing(pointcut = "logsController()" , throwing="exc")
    public void logAfterThrowing(JoinPoint joinPoint, Exception exc) {
        LOGGER.error("Exception in {}.{}() with cause = {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                exc.getMessage() != null ? exc.getMessage() : "NULL");
    }
}

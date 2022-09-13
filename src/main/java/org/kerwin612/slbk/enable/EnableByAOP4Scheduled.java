package org.kerwin612.slbk.enable;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@Aspect
public class EnableByAOP4Scheduled extends EnableByAOP {

    private static final Logger logger = LoggerFactory.getLogger(EnableByAOP4Scheduled.class);

    @Autowired(required = false)
    EnableByAOP4LogKey enableLogKey;

    @PostConstruct
    void init() {
        if (logger.isDebugEnabled()) {
            logger.debug("EnableByAOP4Scheduled enabled!");
        }
    }

    @Around("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public Object scheduled(ProceedingJoinPoint joinPoint) throws Throwable {
        return super.aop(
                joinPoint,
                (method, args) ->
                        String.format(
                                "schedules/%s/%s", method.getDeclaringClass().getSimpleName(), method.getName()), enableLogKey);
    }

}

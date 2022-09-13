package org.kerwin612.slbk.enable;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.kerwin612.slbk.SLBKLogKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;

@Aspect
public class EnableByAOP4LogKey extends EnableByAOP {

    private static final Logger logger = LoggerFactory.getLogger(EnableByAOP4LogKey.class);

    @PostConstruct
    void init() {
        if (logger.isDebugEnabled()) {
            logger.debug("EnableByAOP4LogKey enabled!");
        }
    }

    @Around(
            "@annotation(org.kerwin612.slbk.SLBKLogKey) || execution(public * @org.kerwin612.slbk.SLBKLogKey *.*(..)))")
    public Object logKey(ProceedingJoinPoint joinPoint) throws Throwable {
        return super.aop(joinPoint, (method, args) -> getLogKey(method), this);
    }

    public String getLogKey(Method method) {
        try {
            return method.getAnnotation(SLBKLogKey.class).value();
        } catch (Exception e) {
        }
        return null;
    }

}

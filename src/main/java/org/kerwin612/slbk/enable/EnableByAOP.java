package org.kerwin612.slbk.enable;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;

import static org.kerwin612.slbk.SLBK.clear;
import static org.kerwin612.slbk.SLBK.get;
import static org.kerwin612.slbk.SLBK.isBlank;
import static org.kerwin612.slbk.SLBK.isNotBlank;
import static org.kerwin612.slbk.SLBK.put;

public abstract class EnableByAOP {

    @Autowired(required = false)
    GetFilePath getFilePath;

    public Object aop(ProceedingJoinPoint joinPoint, GetFilePath getFilePath, EnableByAOP4LogKey enableLogKey) throws Throwable {
        if (isNotBlank(get())) return joinPoint.proceed();
        GetFilePath _getFilePath = this.getFilePath == null ? getFilePath : this.getFilePath;
        Method _method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String logKey = null;
        if (enableLogKey != null) {
            logKey = enableLogKey.getLogKey(_method);
        }
        if (isBlank(logKey) && _getFilePath == null) {
            _getFilePath =
                    (method, args) ->
                            String.format("%s/%s", method.getDeclaringClass().getSimpleName(), method.getName());
        }
        put(isBlank(logKey) ? _getFilePath.get(_method, joinPoint.getArgs()) : logKey);
        try {
            return joinPoint.proceed();
        } finally {
            clear();
        }
    }

    public interface GetFilePath {
        String get(Method method, Object[] args);
    }

}
